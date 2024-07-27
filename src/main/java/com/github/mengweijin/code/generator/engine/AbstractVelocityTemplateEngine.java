package com.github.mengweijin.code.generator.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.util.GeneratorUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
@Slf4j
@Getter
public abstract class AbstractVelocityTemplateEngine implements ITemplateEngine {

    private VelocityEngine velocityEngine;

    private Config config;

    private List<TableInfo> tableInfoList;

    static {
        try {
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException e) {
            // velocity1.x的生成格式错乱 https://github.com/baomidou/generator/issues/5
            log.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }
    }

    protected abstract VelocityEngine getVelocityEngine(String templateDir);

    protected abstract List<String> getTemplates();

    @Override
    public ITemplateEngine init(Config config, List<TableInfo> tableInfoList) {
        this.config = config;
        this.tableInfoList = tableInfoList;
        this.velocityEngine = this.getVelocityEngine(config.getTemplateDir());
        return this;
    }

    @Override
    public void write(){
        this.tableInfoList.forEach(tableInfo -> {
            Map<String, Object> objectMap = this.getObjectMap(this.config, tableInfo);

            List<String> templateFilePathList = this.getTemplates();
            if (CollUtil.isEmpty(templateFilePathList)) {
                throw new RuntimeException("No template files found in location " + this.config.getTemplateDir());
            } else {
                String message = "Found " + templateFilePathList.size() + " template files in location " + this.config.getTemplateDir();
                log.info(message);
            }

            templateFilePathList.forEach(tpl -> {
                String tplName = tpl.contains("/") ? StrUtil.subAfter(tpl, "/", true) : tpl;
                String outputFileName = StrUtil.subBefore(GeneratorUtils.renderString(tplName, objectMap), ".", true);
                String outputFilePath = config.getOutputDir() + "/" + outputFileName;
                FileUtil.mkParentDirs(outputFilePath);
                this.write(objectMap, tpl, FileUtil.file(outputFilePath));
            });
        });
    }

    /**
     *
     * @param objectMap template engine args
     * @param templatePath E.g. 1 file system: demo.vm
     *                     E.g. 2 classpath: generator/mybatis/demo.vm
     * @param outputFile E.g.: D:/code/vitality/Demo.java
     */
    private void write(Map<String, Object> objectMap, String templatePath, File outputFile) {
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            VelocityContext context = new VelocityContext(objectMap);
            template.merge(context, writer);
            log.info("生成成功！模板：{}；文件：{}", templatePath, outputFile);
        } catch (IOException e) {
            log.error("生成失败！模板:{};  文件:{}", templatePath, outputFile);
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
