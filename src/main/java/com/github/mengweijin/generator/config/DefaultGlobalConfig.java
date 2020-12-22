package com.github.mengweijin.generator.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.Parameters;

import java.io.File;
import java.util.Optional;

/**
 * @author mengweijin
 */
public class DefaultGlobalConfig extends GlobalConfig {

    private CodeGenerator codeGenerator;

    public DefaultGlobalConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.init();
    }

    /**
     * Initialize the default parameter.
     */
    public void init() {
        Parameters parameters = codeGenerator.getParameters();
        File baseDir = codeGenerator.getBaseDir();

        this.setAuthor(Optional.ofNullable(parameters.getAuthor()).orElse(SystemUtil.getUserInfo().getName()));

        if(StrUtil.isBlank(parameters.getOutputDir())) {
            File output = FileUtil.file(baseDir, "target/code-generator/");
            this.setOutputDir(output.getAbsolutePath());
        } else {
            this.setOutputDir(parameters.getOutputDir());
        }

        this.setOpen(false);
    }
}
