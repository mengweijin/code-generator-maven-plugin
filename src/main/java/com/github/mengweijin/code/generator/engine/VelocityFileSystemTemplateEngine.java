package com.github.mengweijin.code.generator.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.enums.TemplateType;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.dromara.hutool.core.io.file.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
public class VelocityFileSystemTemplateEngine extends AbstractVelocityTemplateEngine {

    /**
     * file system
     * @param templateDir absolute path. E.g.: D:/code/template
     * @return VelocityEngine
     */
    @Override
    protected VelocityEngine getVelocityEngine(String templateDir) {
        Properties p = new Properties();
        p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templateDir);
        p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
        p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
        return new VelocityEngine(p);
    }

    @Override
    protected List<String> getTemplates() {
        Config config = this.getConfig();
        List<File> loopFiles = FileUtil.loopFiles(config.getTemplateDir(), file -> file.isFile() && file.getName().toLowerCase().endsWith(TemplateType.velocity.getSuffix()));
        return loopFiles.stream().map(File::getName).collect(Collectors.toList());
    }
}
