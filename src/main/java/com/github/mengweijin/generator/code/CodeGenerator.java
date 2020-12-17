package com.github.mengweijin.generator.code;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.generator.config.ConfigFactory;
import com.github.mengweijin.generator.config.InjectionConfigImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author mengweijin
 */
@Slf4j
public class CodeGenerator {

    /**
     * 代码生成器
     */
    private final AutoGenerator autoGenerator = new AutoGenerator();

    private final ConfigProperty configProperty;

    public CodeGenerator(ConfigProperty configProperty) {
        this.configProperty = configProperty;
    }

    public void run() {
        // 全局配置
        autoGenerator.setGlobalConfig(ConfigFactory.getGlobalConfig(configProperty));
        // 数据源配置
        autoGenerator.setDataSource(ConfigFactory.getDataSourceConfig(configProperty));
        // 包配置
        autoGenerator.setPackageInfo(ConfigFactory.getPackageConfig(configProperty));
        // Mybatis-plus自己的模板配置
        autoGenerator.setTemplate(ConfigFactory.getTemplateConfig(configProperty));

        // 自定义配置, 会被优先输出
        InjectionConfig injectionConfig = new InjectionConfigImpl(configProperty, autoGenerator);
        List<FileOutConfig> fileOutConfigList = FileOutConfigUtils.loadTemplatesToGetFileOutConfig(configProperty, autoGenerator);
        boolean notContainExistsFiles = FileOutConfigUtils.checkNotContainExistsFiles(fileOutConfigList);
        if (notContainExistsFiles) {
            log.error("Generator failure!!!");
            return;
        }
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        autoGenerator.setCfg(injectionConfig);

        // 策略配置
        autoGenerator.setStrategy(ConfigFactory.getStrategyConfig(configProperty));
        // 模板引擎
        autoGenerator.setTemplateEngine(ConfigFactory.getTemplateEngine(configProperty));

        autoGenerator.execute();
    }

}
