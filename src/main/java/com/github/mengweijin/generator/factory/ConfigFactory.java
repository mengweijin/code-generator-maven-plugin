package com.github.mengweijin.generator.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.github.mengweijin.generator.entity.DbInfo;
import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.config.CustomerDataSource;
import com.github.mengweijin.generator.config.CustomerFileOutConfig;
import com.github.mengweijin.generator.config.CustomerInjectionConfig;
import com.github.mengweijin.generator.util.DbInfoUtils;
import lombok.Setter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author mengweijin
 */
public final class ConfigFactory {

    @Setter
    private ProjectInfo projectInfo;

    @Setter
    private Parameters parameters;

    private static final ConfigFactory configFactory = new ConfigFactory();

    private ConfigFactory() {
    }

    public static ConfigFactory getInstance(ProjectInfo projectInfo) {
        configFactory.setProjectInfo(projectInfo);
        configFactory.setParameters(projectInfo.getParameters());
        return configFactory;
    }

    public DataSourceConfig getDataSourceConfig() {
        DbInfo dbInfo = DbInfoUtils.getDbInfo(projectInfo);

        // 自定义 CustomerDataSource, 使用自定义的 ClassLoader 加载类获取连接。
        CustomerDataSource dataSource = new CustomerDataSource(
                dbInfo.getUrl(),
                dbInfo.getUsername(),
                dbInfo.getPassword());
        return new DataSourceConfig.Builder(dataSource).build();
    }

    public GlobalConfig getGlobalConfig() {
        return GeneratorBuilder.globalConfigBuilder()
                .fileOverride(true)
                .openDir(false)
                .kotlin(false)
                .swagger2(false)
                .outputDir(FileUtil.file(projectInfo.getBaseDir(), "target/code-generator/").getAbsolutePath())
                .author(parameters.getAuthor())
                .dateType(DateType.TIME_PACK).commentDate("yyyy-MM-dd")
                .build();
    }

    public PackageConfig getPackageConfig() {
        return new PackageConfig.Builder()
                .parent(parameters.getOutputPackage())
                .build();
    }

    /**
     * 禁用默认模板，我们需要使用自定义的模板
     * @return
     */
    public TemplateConfig getTemplateConfig() {
        return new TemplateConfig.Builder().disable().build();
    }

    public StrategyConfig getStrategyConfig() {
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
                .enableBaseResultMap()

                .build();
    }

    public InjectionConfig getInjectionConfig(String outputDir, String outputPackage) {
        InjectionConfig injectionConfig = new CustomerInjectionConfig(this.parameters);
        Parameters parameters = projectInfo.getParameters();

        List<File> templateFileList = FileUtil.loopFiles(parameters.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameters.getTemplateType().getSuffix()));

        if (CollectionUtil.isEmpty(templateFileList)) {
            throw new RuntimeException("No template files found in location " + parameters.getTemplateLocation());
        } else {
            String message = "Found " + templateFileList.size() + " template files in location " + parameters.getTemplateLocation();
            System.out.println(message);
        }

        templateFileList.forEach(file -> {
            FileOutConfig fileOutConfig = new CustomerFileOutConfig(file.getAbsolutePath(), outputDir, outputPackage);
            injectionConfig.addFileOutConfig(fileOutConfig);
        });

        return injectionConfig;
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
            Class<?> cls = Class.forName(this.parameters.getSuperEntityClass(), true, classLoader);
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
            return null;
        }
        return Arrays.stream(items).map(String::trim).toArray(String[]::new);
    }
}
