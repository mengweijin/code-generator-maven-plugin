package com.github.mengweijin.code.generator.mojo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.engine.ITemplateEngine;
import com.github.mengweijin.code.generator.engine.VelocityFileSystemTemplateEngine;
import com.github.mengweijin.code.generator.enums.TemplateType;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.util.List;

/**
 * @author mengweijin
 */
@Slf4j
public abstract class AbstractFileSystemGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void checkTemplateExists() throws RuntimeException {
        Config config = this.getConfig();
        File baseDir = this.getBaseDir();
        if(StrUtil.isBlank(config.getTemplateDir())) {
            throw new RuntimeException("No template location is configured！");
        }
        String templateLocation = baseDir.getAbsolutePath() + StrUtil.addPrefixIfNot(config.getTemplateDir(), "/");
        List<File> templateFileList = FileUtil.loopFiles(templateLocation, file -> file.isFile() && file.getName().toLowerCase().endsWith(TemplateType.velocity.getSuffix()));
        if(CollectionUtils.isEmpty(templateFileList)) {
            throw new RuntimeException("No template file exists in path " + templateLocation);
        }
    }

    @Override
    protected void initConfigTemplateDir() {
        Config config = this.getConfig();
        File baseDir = this.getBaseDir();
        String templateDir = baseDir.getAbsolutePath() + StrUtil.addPrefixIfNot(config.getTemplateDir(), "/");
        config.setTemplateDir(templateDir);
    }

    @Override
    protected ITemplateEngine getTemplateEngine() {
        return new VelocityFileSystemTemplateEngine();
    }

}
