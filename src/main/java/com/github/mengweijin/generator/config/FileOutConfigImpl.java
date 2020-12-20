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

    /**
     *
     * @param autoGenerator
     * @param templatePath E.g.:templates/jpa/controller.java.btl
     */
    public FileOutConfigImpl(AutoGenerator autoGenerator, String templatePath) {
        super(templatePath);
        this.autoGenerator = autoGenerator;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        StringBuilder outputPath = new StringBuilder();
        String outputDir = autoGenerator.getGlobalConfig().getOutputDir();
        outputPath.append(outputDir);
        if(!outputDir.endsWith("/") && !outputDir.endsWith("\\")) {
            outputPath.append(File.separator);
        }
        StringBuilder componentName = new StringBuilder();
        String[] packageHierarchy = this.getTemplatePath().substring(this.getTemplatePath().lastIndexOf("/") + 1).split("\\.");
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
