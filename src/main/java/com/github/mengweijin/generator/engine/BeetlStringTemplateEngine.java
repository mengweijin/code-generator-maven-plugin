package com.github.mengweijin.generator.engine;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author mengweijin
 */
@Slf4j
public class BeetlStringTemplateEngine extends AbstractTemplateEngine {

    private GroupTemplate groupTemplate;

    @Override
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        try {
            Configuration cfg = new Configuration(ClassLoader.getSystemClassLoader());
            groupTemplate = new GroupTemplate(new StringTemplateResourceLoader(), cfg);
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception {
        // read template content from template path
        String templateContent = FileUtil.readUtf8String(templatePath);
        Template template = groupTemplate.getTemplate(templateContent);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.binding(objectMap);
            template.renderTo(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        log.info("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".btl";
    }
}
