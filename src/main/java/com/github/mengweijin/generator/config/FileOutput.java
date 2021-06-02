package com.github.mengweijin.generator.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.github.mengweijin.generator.CustomerAutoGenerator;
import com.github.mengweijin.generator.entity.IdField;
import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
@Slf4j
public class FileOutput {

    /**
     *
     * @param tableInfo Table info
     * @param objectMap Table object map
     * @param customerAutoGenerator CustomerAutoGenerator
     */
    public static void outputFile(
                                  TableInfo tableInfo,
                                  Map<String, Object> objectMap,
                                  CustomerAutoGenerator customerAutoGenerator) {

        ProjectInfo projectInfo = customerAutoGenerator.getProjectInfo();

        Parameters parameters = projectInfo.getParameters();
        enhanceObjectMap(objectMap, projectInfo.getParameters());

        AbstractTemplateEngine templateEngine = customerAutoGenerator.templateEngine();
        String outputDir = customerAutoGenerator.globalConfigBuilder().build().getOutputDir();
        String outputPackage = customerAutoGenerator.packageConfigBuilder().build().getParent();

        List<File> templateFileList = FileUtil.loopFiles(parameters.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameters.getTemplateType().getSuffix()));

        if (CollectionUtil.isEmpty(templateFileList)) {
            throw new RuntimeException("No template files found in location " + parameters.getTemplateLocation());
        } else {
            String message = "Found " + templateFileList.size() + " template files in location " + parameters.getTemplateLocation();
            log.info(message);
        }

        try {
            for (File templateFile: templateFileList) {
                // 初始化输出文件的名称和路径
                File outputFile = buildOutputFile(tableInfo, templateFile.getAbsolutePath(), outputDir, outputPackage);
                FileUtil.mkParentDirs(outputFile);
                // 使用模板引擎，渲染并输出文件
                templateEngine.writer(objectMap, templateFile.getAbsolutePath(), outputFile);
            }
        } catch (Exception e) {
            log.error("Template engine writer error!", e);
            throw new RuntimeException(e);
        }
    }

    private static void enhanceObjectMap(Map<String, Object> objectMap, Parameters parameters) {
        objectMap.remove("package");
        objectMap.put("parameters", parameters);
        objectMap.put("idField", getIdField((TableInfo) objectMap.get("table")));
        objectMap.put("allFieldList", handleAllFieldList((TableInfo) objectMap.get("table")));

        log.info("Beetl parameter map: {}", objectMap);
    }

    /**
     *
     * @param tableInfo
     * @param templatePath 绝对路径
     * @param outputDir
     * @param outputPackage
     * @return 全路径名文件
     */
    private static File buildOutputFile(TableInfo tableInfo, String templatePath, String outputDir, String outputPackage) {
        StringBuilder outputPath = new StringBuilder(outputDir);

        if(!outputDir.endsWith(StrUtil.SLASH) && !outputDir.endsWith(StrUtil.BACKSLASH)) {
            outputPath.append(File.separator);
        }
        if(!StrUtil.isBlank(outputPackage)) {
            outputPath.append(outputPackage).append(File.separator);
        }

        StringBuilder componentName = new StringBuilder();
        File templateFile = FileUtil.file(templatePath);
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

    private static IdField getIdField(TableInfo tableInfo) {
        TableField tableField = tableInfo.getFields().stream().filter(TableField::isKeyFlag).findFirst().orElse(null);

        IdField idField = new IdField();
        if(tableField != null) {
            idField.setColumnName(tableField.getName());
            idField.setPropertyName(tableField.getPropertyName());
            idField.setPropertyType(tableField.getColumnType().getType());
        }
        return idField;
    }

    private static List<TableField> handleAllFieldList(TableInfo tableInfo) {
        List<TableField> fieldList = tableInfo.getFields();
        List<TableField> commonFields = tableInfo.getCommonFields();

        // 为了不影响其他地方的引用，这里必须创建一个新的集合来存放所有字段
        List<TableField> allList = new ArrayList<>();
        allList.addAll(fieldList);
        allList.addAll(commonFields);
        return allList;
    }
}
