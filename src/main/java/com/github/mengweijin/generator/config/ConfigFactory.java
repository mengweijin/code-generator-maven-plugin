package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.github.mengweijin.generator.code.ConfigProperty;
import com.github.mengweijin.generator.code.ETemplateSuffix;

import java.io.File;

public class ConfigFactory {

    public static GlobalConfig getGlobalConfig(ConfigProperty configProperty) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + File.separator + "src/main/java");
        globalConfig.setAuthor(configProperty.getAuthor());
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(false);
        return globalConfig;
    }

    public static DataSourceConfig getDataSourceConfig(ConfigProperty configProperty) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(configProperty.getDbUrl());
        dataSourceConfig.setDriverName(configProperty.getDbDriverName());
        dataSourceConfig.setUsername(configProperty.getDbUsername());
        dataSourceConfig.setPassword(configProperty.getDbPassword());
        return dataSourceConfig;
    }

    public static PackageConfig getPackageConfig(ConfigProperty configProperty) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(configProperty.getOutputPackage().substring(0, configProperty.getOutputPackage().lastIndexOf(".")));
        packageConfig.setModuleName(configProperty.getOutputPackage().substring(configProperty.getOutputPackage().lastIndexOf(".") + 1));
        packageConfig.setEntity(null);
        packageConfig.setService(null);
        packageConfig.setServiceImpl(null);
        packageConfig.setMapper(null);
        packageConfig.setXml(null);
        packageConfig.setController(null);
        packageConfig.setPathInfo(null);
        return packageConfig;
    }

    /**
     * Mybatis-plus自己的模板配置, 不想生成的就设为null。
     * 这里不使用Mybatis-plus自己的，我们使用自定义的
     *
     * @param configProperty
     * @return
     */
    public static TemplateConfig getTemplateConfig(ConfigProperty configProperty) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(null);
        templateConfig.setEntityKt(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        templateConfig.setController(null);
        return templateConfig;
    }

    public static StrategyConfig getStrategyConfig(ConfigProperty configProperty) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        strategy.setSuperEntityClass(configProperty.getSuperEntityClass());
        strategy.setSuperEntityColumns(configProperty.getSuperEntityColumns());
        strategy.setSuperMapperClass(configProperty.getSuperDaoClass());
        strategy.setSuperServiceClass(configProperty.getSuperServiceClass());
        strategy.setSuperServiceImplClass(configProperty.getSuperServiceImplClass());
        strategy.setSuperControllerClass(configProperty.getSuperControllerClass());

        strategy.setInclude(configProperty.getTables());
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(configProperty.getTablePrefix());
        return strategy;
    }

    public static AbstractTemplateEngine getTemplateEngine(ConfigProperty configProperty) {
        AbstractTemplateEngine templateEngine;
        String suffix = configProperty.getTemplateSuffix();
        if (ETemplateSuffix.velocity.getSuffix().equalsIgnoreCase(suffix)) {
            templateEngine = new VelocityTemplateEngine();
        } else if (ETemplateSuffix.freemarker.getSuffix().equalsIgnoreCase(suffix)) {
            templateEngine = new FreemarkerTemplateEngine();
        } else {
            // default btl
            templateEngine = new BeetlTemplateEngine();
        }

        return templateEngine;
    }

}
