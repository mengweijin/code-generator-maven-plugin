package com.github.mengweijin.generator.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.Parameters;
import com.github.mengweijin.generator.config.CustomerFileOutConfig;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author mengweijin
 */
public class FileOutConfigUtils {

    public static List<FileOutConfig> loadTemplatesToGetFileOutConfig(CodeGenerator codeGenerator) {
        Parameters parameters = codeGenerator.getParameters();
        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(parameters.getTemplateLocation());
        if (URLUtil.isFileURL(url)) {
            fileOutConfigList = FileOutConfigUtils.resolveByFileSystem(codeGenerator);
        } else if (URLUtil.isJarURL(url)) {
            JarFile jarFile = URLUtil.getJarFile(url);
            fileOutConfigList = FileOutConfigUtils.resolveByJarFile(codeGenerator, jarFile);
        }
        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByFileSystem(CodeGenerator codeGenerator) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        Parameters parameters = codeGenerator.getParameters();

        List<File> fileList = FileUtil.loopFiles(parameters.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameters.getTemplateType().name()));

        if (CollectionUtil.isEmpty(fileList)) {
            throw new RuntimeException("No template files found in location " + parameters.getTemplateLocation());
        }

        String message = "Found " + fileList.size() + "template files in location " + parameters.getTemplateLocation();
        System.out.println(message);

        fileList.forEach(file -> {
            // 自定义配置会被优先输出
            String templatePath = codeGenerator.getBaseDir().getAbsolutePath() + File.separator + parameters.getTemplateLocation() + file.getName();
            FileOutConfig fileOutConfig = new CustomerFileOutConfig(codeGenerator, templatePath);
            fileOutConfigList.add(fileOutConfig);
        });

        return fileOutConfigList;
    }

    // TODO 在MOJO入口处，对jar包里面的模板文件先做拷贝到文件系统，然后统一使用文件系统绝对路径。就可以删除这个方法了
    private static List<FileOutConfig> resolveByJarFile(CodeGenerator codeGenerator, JarFile jarFile) {
        Parameters parameters = codeGenerator.getParameters();
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<JarEntry> enumeration = jarFile.entries();
        String jarEntryName;
        while (enumeration.hasMoreElements()) {
            jarEntryName = enumeration.nextElement().getName();
            if (jarEntryName.startsWith(parameters.getTemplateLocation()) && !jarEntryName.endsWith("/")) {
                String templatePath = FileUtil.readString(classLoader.getResource(jarEntryName), "UTF-8");
                FileOutConfig fileOutConfig = new CustomerFileOutConfig(codeGenerator, templatePath);
                fileOutConfigList.add(fileOutConfig);
            }
        }

        return fileOutConfigList;
    }
}
