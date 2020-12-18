package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;

public class FileOutConfigImpl extends FileOutConfig {

    private final AutoGenerator autoGenerator;

    /**
     * E.g.:templates/jpa/controller.java.btl
     */
    private final String templateFile;

    public FileOutConfigImpl(AutoGenerator autoGenerator, String templateFile) {
        this.autoGenerator = autoGenerator;
        this.templateFile = templateFile;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        StringBuilder outputPath = new StringBuilder();
        outputPath.append(autoGenerator.getGlobalConfig().getOutputDir()).append(File.separator);
        outputPath.append(autoGenerator.getPackageInfo().getParent().replace(StringPool.DOT, File.separator)).append(File.separator);
        StringBuilder componentName = new StringBuilder();
        String[] packageHierarchy = templateFile.substring(templateFile.lastIndexOf("/") + 1).split("\\.");
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
