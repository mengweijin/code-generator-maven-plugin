package com.github.mengweijin.generator.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
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
import com.github.mengweijin.generator.DbInfo;
import com.github.mengweijin.generator.Parameters;
import com.github.mengweijin.generator.ProjectInfo;
import com.github.mengweijin.generator.config.CustomerFileOutConfig;
import com.github.mengweijin.generator.util.DataSourceConfigUtils;
import lombok.Setter;

import java.io.File;
import java.sql.Connection;
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
        DbInfo dbInfo = DataSourceConfigUtils.getDbInfo(projectInfo);
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                dbInfo.getUrl(),
                dbInfo.getUsername(),
                dbInfo.getPassword())
            .build();

        Connection connection = DataSourceConfigUtils.getConnection(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
        ReflectUtil.setFieldValue(dataSourceConfig, "connection", connection);

        return dataSourceConfig;
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
                .addInclude(parameters.getTables())
                .addTablePrefix(parameters.getTablePrefix())

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
                //.addSuperEntityColumns()

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

        InjectionConfig injectionConfig = new InjectionConfig();
        Parameters parameters = projectInfo.getParameters();

        List<File> templateFileList = FileUtil.loopFiles(parameters.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameters.getTemplateType().name()));

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

}
