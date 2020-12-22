package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.github.mengweijin.generator.CodeGenerator;

/**
 * @author mengweijin
 */
public class DefaultTemplateConfig extends TemplateConfig {

    private CodeGenerator codeGenerator;

    public DefaultTemplateConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.init();
    }

    /**
     * Initialize the default parameter.
     * Mybatis-plus自己的模板配置, 不想生成的就强制设为null。
     * 这里不使用Mybatis-plus自己的，我们使用自定义的
     */
    public void init() {
        this.setEntity(null);
        this.setEntityKt(null);
        this.setService(null);
        this.setServiceImpl(null);
        this.setMapper(null);
        this.setXml(null);
        this.setController(null);
    }
}
