package com.github.mengweijin.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.SimpleAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.IConfigBuilder;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.github.mengweijin.generator.config.CustomerDataSource;
import com.github.mengweijin.generator.config.FileOutput;
import com.github.mengweijin.generator.entity.DbInfo;
import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.factory.TemplateEngineFactory;
import com.github.mengweijin.generator.util.DbInfoUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author mengweijin
 */
@Slf4j
public class CustomerAutoGenerator extends SimpleAutoGenerator {

    @Getter
    @Setter
    private ProjectInfo projectInfo;

    private static final CustomerAutoGenerator instance = new CustomerAutoGenerator();

    private CustomerAutoGenerator() {
    }

    public static CustomerAutoGenerator getInstance(ProjectInfo projectInfo) {
        instance.setProjectInfo(projectInfo);
        return instance;
    }

    @Override
    public IConfigBuilder<DataSourceConfig> dataSourceConfigBuilder() {
        DbInfo dbInfo = DbInfoUtils.getDbInfo(projectInfo);

        // 自定义 CustomerDataSource, 使用自定义的 ClassLoader 加载类获取连接。
        CustomerDataSource dataSource = new CustomerDataSource(
                dbInfo.getUrl(),
                dbInfo.getUsername(),
                dbInfo.getPassword());
        return new DataSourceConfig.Builder(dataSource);
    }

    @Override
    public IConfigBuilder<GlobalConfig> globalConfigBuilder() {
        return new GlobalConfig.Builder().fileOverride()
                .enableSwagger()
                .openDir(false)
                .outputDir(FileUtil.file(projectInfo.getBaseDir(), "target/code-generator/").getAbsolutePath())
                .author(projectInfo.getParameters().getAuthor())
                .dateType(DateType.TIME_PACK).commentDate("yyyy-MM-dd");
    }

    @Override
    public IConfigBuilder<PackageConfig> packageConfigBuilder() {
        return new PackageConfig.Builder().parent(projectInfo.getParameters().getOutputPackage());
    }

    /**
     * 自定义模板配置 Builder
     * 禁用默认模板，我们需要使用自定义的模板
     * @return templateConfigBuilder
     */
    @Override
    public IConfigBuilder<TemplateConfig> templateConfigBuilder() {
        return new TemplateConfig.Builder().disable();
    }

    @Override
    public IConfigBuilder<StrategyConfig> strategyConfigBuilder() {
        Parameters parameters = projectInfo.getParameters();
        return new StrategyConfig.Builder()
                .addInclude(this.trimItemName(parameters.getTables()))
                .addTablePrefix(this.trimItemName(parameters.getTablePrefix()))

                .entityBuilder()
                .superClass(parameters.getSuperEntityClass())
                .enableSerialVersionUID()
                .enableChainModel()
                .enableLombok()
                .enableTableFieldAnnotation()
                // 乐观锁数据库列名
                .versionColumnName("version")
                // 乐观锁 Entity 属性名
                .versionPropertyName("version")
                // 逻辑删除数据库列名
                .logicDeleteColumnName("deleted")
                // 逻辑删除 Entity 属性名
                .logicDeletePropertyName("deleted")
                // 数据库表映射到实体的命名策略
                .naming(NamingStrategy.underline_to_camel)
                .addSuperEntityColumns(this.generateDefaultSuperEntityColumns())

                .controllerBuilder()
                .superClass(parameters.getSuperControllerClass())
                // 开启驼峰转连字符
                .enableHyphenStyle()
                // 开启生成@RestController控制器
                .enableRestStyle()

                .serviceBuilder()
                .superServiceClass(parameters.getSuperServiceClass())
                .superServiceImplClass(parameters.getSuperServiceImplClass())

                .mapperBuilder()
                .superClass(parameters.getSuperDaoClass())
                .enableBaseColumnList()
                .enableBaseResultMap();
    }

    @Override
    public AbstractTemplateEngine templateEngine() {
        return TemplateEngineFactory.getTemplateEngine(this.projectInfo.getParameters().getTemplateType());
    }

    @Override
    public IConfigBuilder<InjectionConfig> injectionConfigBuilder() {
        return new InjectionConfig.Builder().beforeOutputFile((tableInfo, objectMap) -> {
            FileOutput.outputFile(tableInfo, objectMap, this);
        });
    }

    /**
     * If the user configured superEntityColumns, the configuration will prevail;
     * if not, the default configuration of superEntityColumns will be generated according to the superEntityClass.
     *
     * @return String[]
     */
    private String[] generateDefaultSuperEntityColumns() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName(projectInfo.getParameters().getSuperEntityClass(), true, classLoader);
            Field[] declaredFields = ClassUtil.getDeclaredFields(cls);
            return Arrays.stream(declaredFields)
                    .map(field -> StrUtil.toUnderlineCase(field.getName())).toArray(String[]::new);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String[] trimItemName(String[] items) {
        if(items == null) {
            return new String[]{};
        }
        return Arrays.stream(items).map(String::trim).toArray(String[]::new);
    }

}
