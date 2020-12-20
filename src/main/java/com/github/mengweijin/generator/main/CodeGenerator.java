package com.github.mengweijin.generator.main;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.generator.config.ConfigFactory;
import com.github.mengweijin.generator.config.InjectionConfigImpl;
import com.github.mengweijin.generator.dto.DefaultConfigParameter;
import com.github.mengweijin.generator.util.FileOutConfigUtils;

import java.util.List;

/**
 * @author mengweijin
 */
public class CodeGenerator {

    /**
     * 代码生成器
     */
    private final AutoGenerator autoGenerator = new AutoGenerator();

    private final DefaultConfigParameter parameter;

    public CodeGenerator(DefaultConfigParameter parameter) {
        this.parameter = parameter;
    }

    public void run() {
        // 全局配置
        autoGenerator.setGlobalConfig(ConfigFactory.getGlobalConfig(parameter));
        // 数据源配置
        autoGenerator.setDataSource(ConfigFactory.getDataSourceConfig(parameter));
        // 包配置
        autoGenerator.setPackageInfo(ConfigFactory.getPackageConfig(parameter));
        // Mybatis-plus自己的模板配置
        autoGenerator.setTemplate(ConfigFactory.getTemplateConfig(parameter));

        // 自定义配置, 会被优先输出
        InjectionConfig injectionConfig = new InjectionConfigImpl(parameter, autoGenerator);
        List<FileOutConfig> fileOutConfigList = FileOutConfigUtils.loadTemplatesToGetFileOutConfig(parameter, autoGenerator);

        // TODO check file exits.


        injectionConfig.setFileOutConfigList(fileOutConfigList);
        autoGenerator.setCfg(injectionConfig);

        // 策略配置
        autoGenerator.setStrategy(ConfigFactory.getStrategyConfig(parameter));
        // 模板引擎
        autoGenerator.setTemplateEngine(ConfigFactory.getTemplateEngine(parameter));

        autoGenerator.execute();
    }

}
