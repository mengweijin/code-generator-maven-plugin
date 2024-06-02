package com.github.mengweijin.code.generator.mojo;

import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.engine.ITemplateEngine;
import com.github.mengweijin.code.generator.engine.VelocityClasspathTemplateEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 */
@Slf4j
public abstract class AbstractClasspathGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void checkTemplateExists() throws RuntimeException {
        // ignore for classpath
    }

    @Override
    protected void initConfigTemplateDir() {
        Config config = this.getConfig();
        config.setTemplateDir(this.getTemplateDir());
    }

    @Override
    protected ITemplateEngine getTemplateEngine() {
        return new VelocityClasspathTemplateEngine();
    }

    protected abstract String getTemplateDir();

}
