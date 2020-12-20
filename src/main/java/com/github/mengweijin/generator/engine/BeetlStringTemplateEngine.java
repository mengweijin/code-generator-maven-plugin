package com.github.mengweijin.generator.engine;

import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author mengweijin
 */
public class BeetlStringTemplateEngine extends BeetlTemplateEngine {

    private GroupTemplate groupTemplate;

    @Override
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            groupTemplate = new GroupTemplate(resourceLoader, cfg);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return this;
    }

    /**
     *
     * @param objectMap
     * @param templateString template String
     * @param outputFile
     * @throws Exception
     */
    @Override
    public void writer(Map<String, Object> objectMap, String templateString, String outputFile) throws Exception {
        Template template = groupTemplate.getTemplate(templateString);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.binding(objectMap);
            template.renderTo(fileOutputStream);
        }
        logger.debug("文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".btl";
    }
}
