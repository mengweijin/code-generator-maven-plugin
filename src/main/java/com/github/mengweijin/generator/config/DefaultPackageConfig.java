package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.github.mengweijin.generator.CodeGenerator;

/**
 * @author mengweijin
 */
public class DefaultPackageConfig extends PackageConfig {

    private CodeGenerator codeGenerator;

    public DefaultPackageConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.init();
    }

    /**
     * Initialize the default parameter.
     */
    public void init() {
        //String moduleName = StrUtil.subAfter(parameter.getOutputDir(), StrUtil.SLASH, true);
        this.setParent("com.github.mengweijin");
        this.setModuleName(null);
        this.setEntity(null);
        this.setService(null);
        this.setServiceImpl(null);
        this.setMapper(null);
        this.setXml(null);
        this.setController(null);
        this.setPathInfo(null);
    }
}
