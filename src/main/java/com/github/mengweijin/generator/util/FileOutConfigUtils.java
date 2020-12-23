package com.github.mengweijin.generator.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.Parameters;
import com.github.mengweijin.generator.config.CustomerFileOutConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mengweijin
 */
public class FileOutConfigUtils {

    public static List<FileOutConfig> loadTemplatesToGetFileOutConfig(CodeGenerator codeGenerator) {
        Parameters parameters = codeGenerator.getParameters();
        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();;

        List<File> templateFileList = FileUtil.loopFiles(parameters.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameters.getTemplateType().name()));

        if (CollectionUtil.isEmpty(templateFileList)) {
            throw new RuntimeException("No template files found in location " + parameters.getTemplateLocation());
        } else {
            String message = "Found " + templateFileList.size() + "template files in location " + parameters.getTemplateLocation();
            System.out.println(message);
        }

        templateFileList.forEach(file -> {
            FileOutConfig fileOutConfig = new CustomerFileOutConfig(codeGenerator, file.getAbsolutePath());
            fileOutConfigList.add(fileOutConfig);
        });
        return fileOutConfigList;
    }
}
