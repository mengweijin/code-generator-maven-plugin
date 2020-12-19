package com.github.mengweijin.generator.main;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.dto.ConfigParameter;
import com.github.mengweijin.generator.config.ConfigFactory;
import com.github.mengweijin.generator.config.InjectionConfigImpl;
import com.github.mengweijin.generator.util.FileOutConfigUtils;
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

    private final ConfigParameter configParameter;

    public CodeGenerator(ConfigParameter configParameter) {
        this.configParameter = configParameter;
    }

    public void run() {
        // 全局配置
        autoGenerator.setGlobalConfig(ConfigFactory.getGlobalConfig(configParameter));
        // 数据源配置
        autoGenerator.setDataSource(ConfigFactory.getDataSourceConfig(configParameter));
        // 包配置
        autoGenerator.setPackageInfo(ConfigFactory.getPackageConfig(configParameter));
        // Mybatis-plus自己的模板配置
        autoGenerator.setTemplate(ConfigFactory.getTemplateConfig(configParameter));

        // 自定义配置, 会被优先输出
        InjectionConfig injectionConfig = new InjectionConfigImpl(configParameter, autoGenerator);
        List<FileOutConfig> fileOutConfigList = FileOutConfigUtils.loadTemplatesToGetFileOutConfig(configParameter, autoGenerator);

        // TODO check file exits.


        injectionConfig.setFileOutConfigList(fileOutConfigList);
        autoGenerator.setCfg(injectionConfig);

        // 策略配置
        autoGenerator.setStrategy(ConfigFactory.getStrategyConfig(configParameter));
        // 模板引擎
        autoGenerator.setTemplateEngine(ConfigFactory.getTemplateEngine(configParameter));

        autoGenerator.execute();
    }

}
