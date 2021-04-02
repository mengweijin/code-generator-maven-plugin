package com.github.mengweijin.generator.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;

/**
 * @author mengweijin
 */
public class CustomerFileOutConfig extends FileOutConfig {

    private String outputDir;

    private String outputPackage;

    /**
     *
     * @param templatePath 绝对全路径
     * @param outputDir FileUtil.file(projectInfo.getBaseDir(), "target/code-generator/"
     * @param outputPackage com.github.mengweijin
     */
    public CustomerFileOutConfig(String templatePath, String outputDir, String outputPackage) {
        super(templatePath);
        this.outputDir = outputDir;
        this.outputPackage = outputPackage;
    }

    /**
     *
     * @param tableInfo
     * @return 全路径名
     */
    @Override
    public File outputFile(TableInfo tableInfo) {
        StringBuilder outputPath = new StringBuilder(outputDir);

        if(!outputDir.endsWith(StrUtil.SLASH) && !outputDir.endsWith(StrUtil.BACKSLASH)) {
            outputPath.append(File.separator);
        }
        if(!StrUtil.isBlank(outputPackage)) {
            outputPath.append(outputPackage).append(File.separator);
        }

        StringBuilder componentName = new StringBuilder();
        File templateFile = FileUtil.file(this.getTemplatePath());
        String[] packageHierarchy = templateFile.getName().split("\\.");
        if ("entity".equalsIgnoreCase(packageHierarchy[0])) {
            outputPath.append(packageHierarchy[0]).append(File.separator);
        } else {
            for (int i = 0; i < packageHierarchy.length - 2; i++) {
                componentName.append(NamingStrategy.capitalFirst(packageHierarchy[i]));
                outputPath.append(packageHierarchy[i].toLowerCase()).append(File.separator);
            }
        }

        outputPath.append(tableInfo.getEntityName()).append(componentName);
        String path = StrUtil.replace(outputPath.toString(), StrUtil.DOT, StrUtil.SLASH);
        return new File(path + StrUtil.DOT + packageHierarchy[packageHierarchy.length - 2]);
    }
}
