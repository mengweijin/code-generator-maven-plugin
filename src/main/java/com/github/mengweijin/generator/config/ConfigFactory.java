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
import com.github.mengweijin.generator.dto.ConfigParameter;
import com.github.mengweijin.generator.dto.DbInfo;
import com.github.mengweijin.generator.enums.TemplateType;

import java.io.File;

public class ConfigFactory {

    public static GlobalConfig getGlobalConfig(ConfigParameter configParameter) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + File.separator + "src/main/java");
        globalConfig.setAuthor(configParameter.getAuthor());
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(false);
        return globalConfig;
    }

    public static DataSourceConfig getDataSourceConfig(ConfigParameter configParameter) {
        DbInfo dbInfo = configParameter.getDbInfo();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(dbInfo.getUrl());
        dataSourceConfig.setDriverName(dbInfo.getDriverName());
        dataSourceConfig.setUsername(dbInfo.getUsername());
        dataSourceConfig.setPassword(dbInfo.getPassword());
        return dataSourceConfig;
    }

    public static PackageConfig getPackageConfig(ConfigParameter configParameter) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(configParameter.getOutputPackage().substring(0, configParameter.getOutputPackage().lastIndexOf(".")));
        packageConfig.setModuleName(configParameter.getOutputPackage().substring(configParameter.getOutputPackage().lastIndexOf(".") + 1));
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
     * @param configParameter
     * @return
     */
    public static TemplateConfig getTemplateConfig(ConfigParameter configParameter) {
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

    public static StrategyConfig getStrategyConfig(ConfigParameter configParameter) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        strategy.setSuperEntityClass(configParameter.getSuperEntityClass());
        strategy.setSuperEntityColumns(configParameter.getSuperEntityColumns());
        strategy.setSuperMapperClass(configParameter.getSuperDaoClass());
        strategy.setSuperServiceClass(configParameter.getSuperServiceClass());
        strategy.setSuperServiceImplClass(configParameter.getSuperServiceImplClass());
        strategy.setSuperControllerClass(configParameter.getSuperControllerClass());

        strategy.setInclude(configParameter.getTables());
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(configParameter.getTablePrefix());
        return strategy;
    }

    public static AbstractTemplateEngine getTemplateEngine(ConfigParameter configParameter) {
        AbstractTemplateEngine templateEngine;
        TemplateType templateType = configParameter.getTemplateType();
        if (TemplateType.velocity == templateType) {
            templateEngine = new VelocityTemplateEngine();
        } else if (TemplateType.freemarker == templateType) {
            templateEngine = new FreemarkerTemplateEngine();
        } else {
            // default beetl
            templateEngine = new BeetlTemplateEngine();
        }

        return templateEngine;
    }

}
