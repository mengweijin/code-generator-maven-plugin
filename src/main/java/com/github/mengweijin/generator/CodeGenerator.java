package com.github.mengweijin.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.factory.ConfigFactory;
import com.github.mengweijin.generator.factory.TemplateEngineFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 */
@Slf4j
public class CodeGenerator {

    private final ProjectInfo projectInfo;

    public CodeGenerator(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    public void run() {
        log.info("ProjectInfo before config: {}", projectInfo);

        ConfigFactory configFactory = ConfigFactory.getInstance(projectInfo);
        DataSourceConfig dataSourceConfig = configFactory.getDataSourceConfig();
        GlobalConfig globalConfig = configFactory.getGlobalConfig();
        PackageConfig packageConfig = configFactory.getPackageConfig();
        TemplateConfig templateConfig = configFactory.getTemplateConfig();
        StrategyConfig strategyConfig = configFactory.getStrategyConfig();
        InjectionConfig injectionConfig = configFactory.getInjectionConfig(globalConfig.getOutputDir(), packageConfig.getParent());

        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig)
                .global(globalConfig)
                .packageInfo(packageConfig)
                .template(templateConfig)
                .strategy(strategyConfig)
                .injection(injectionConfig);

        log.info("ProjectInfo after config: {}", projectInfo);

        AbstractTemplateEngine templateEngine = TemplateEngineFactory.getTemplateEngine(this.projectInfo.getParameters().getTemplateType());
        autoGenerator.execute(templateEngine);
    }
}
