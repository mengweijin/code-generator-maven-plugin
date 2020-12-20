package com.github.mengweijin.generator.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.generator.config.FileOutConfigImpl;
import com.github.mengweijin.generator.dto.DefaultConfigParameter;

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

    public static List<FileOutConfig> loadTemplatesToGetFileOutConfig(DefaultConfigParameter parameter, AutoGenerator autoGenerator) {
        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(parameter.getTemplateLocation());
        if (URLUtil.isFileURL(url)) {
            fileOutConfigList = FileOutConfigUtils.resolveByFileSystem(autoGenerator, parameter);
        } else if (URLUtil.isJarURL(url)) {
            JarFile jarFile = URLUtil.getJarFile(url);
            fileOutConfigList = FileOutConfigUtils.resolveByJarFile(autoGenerator, parameter, jarFile);
        }
        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByFileSystem(AutoGenerator autoGenerator, DefaultConfigParameter parameter) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        List<File> fileList = FileUtil.loopFiles(parameter.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(parameter.getTemplateType().name()));

        if (CollectionUtil.isEmpty(fileList)) {
            throw new RuntimeException("No template files found in location " + parameter.getTemplateLocation());
        }

        String message = "Found " + fileList.size() + "template files in location " + parameter.getTemplateLocation();
        System.out.println(message);

        fileList.forEach(file -> {
            // 自定义配置会被优先输出
            String templatePath = parameter.getBaseDir().getAbsolutePath() + File.separator + parameter.getTemplateLocation() + file.getName();
            String templateName = file.getName();
            FileOutConfig fileOutConfig = new FileOutConfigImpl(autoGenerator, templatePath, templateName);
            fileOutConfigList.add(fileOutConfig);
        });

        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByJarFile(AutoGenerator autoGenerator, DefaultConfigParameter parameter, JarFile jarFile) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<JarEntry> enumeration = jarFile.entries();
        String jarEntryName;
        while (enumeration.hasMoreElements()) {
            jarEntryName = enumeration.nextElement().getName();
            if (jarEntryName.startsWith(parameter.getTemplateLocation()) && !jarEntryName.endsWith("/")) {
                String templateContent = FileUtil.readString(classLoader.getResource(jarEntryName), "UTF-8");
                String templateName = StrUtil.subAfter(jarEntryName, StrUtil.SLASH, true);
                FileOutConfig fileOutConfig = new FileOutConfigImpl(autoGenerator, templateContent, templateName);
                fileOutConfigList.add(fileOutConfig);
            }
        }

        return fileOutConfigList;
    }
}
