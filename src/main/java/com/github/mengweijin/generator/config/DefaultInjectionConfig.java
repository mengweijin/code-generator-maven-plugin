package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.github.mengweijin.generator.CodeGenerator;

import java.util.Map;

/**
 * @author mengweijin
 */
public class DefaultInjectionConfig extends InjectionConfig {

    private CodeGenerator codeGenerator;

    public DefaultInjectionConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void initMap() {
    }

    @Override
    public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
        return objectMap;
    }
}
