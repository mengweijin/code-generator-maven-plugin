package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;

/**
 * @author mengweijin
 */
public class FileOutConfigImpl extends FileOutConfig {

    private final AutoGenerator autoGenerator;

    private String templateName;

    /**
     *
     * @param autoGenerator
     * @param templateContent E.g.:templates/jpa/controller.java.btl 文件中的字符串内容。
     */
    public FileOutConfigImpl(AutoGenerator autoGenerator, String templateContent, String templateName) {
        super(templateContent);
        this.autoGenerator = autoGenerator;
        this.templateName = templateName;
    }

    /**
     *
     * @param tableInfo
     * @return target/code-generator/controller/SysUserController.java
     */
    @Override
    public String outputFile(TableInfo tableInfo) {
        StringBuilder outputPath = new StringBuilder();
        String outputDir = autoGenerator.getGlobalConfig().getOutputDir();
        outputPath.append(outputDir);
        if(!outputDir.endsWith("/") && !outputDir.endsWith("\\")) {
            outputPath.append(File.separator);
        }
        StringBuilder componentName = new StringBuilder();
        String[] packageHierarchy = this.templateName.split("\\.");
        if ("entity".equalsIgnoreCase(packageHierarchy[0])) {
            outputPath.append(packageHierarchy[0]).append(File.separator);
        } else {
            for (int i = 0; i < packageHierarchy.length - 2; i++) {
                componentName.append(NamingStrategy.capitalFirst(packageHierarchy[i]));
                outputPath.append(packageHierarchy[i].toLowerCase()).append(File.separator);
            }
        }

        outputPath.append(tableInfo.getEntityName())
                .append(componentName)
                .append(StringPool.DOT)
                .append(packageHierarchy[packageHierarchy.length - 2]);

        return outputPath.toString();
    }
}
