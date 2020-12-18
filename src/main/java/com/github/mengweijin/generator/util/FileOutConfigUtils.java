package com.github.mengweijin.generator.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.github.mengweijin.dto.ConfigParameter;
import com.github.mengweijin.generator.config.FileOutConfigImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author mengweijin
 */
@Slf4j
public class FileOutConfigUtils {

    public static List<FileOutConfig> loadTemplatesToGetFileOutConfig(ConfigParameter configParameter, AutoGenerator autoGenerator) {
        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(configParameter.getTemplateLocation());
        if (URLUtil.isFileURL(url)) {
            fileOutConfigList = FileOutConfigUtils.resolveByFileSystem(autoGenerator, configParameter);
        } else if (URLUtil.isJarURL(url)) {
            JarFile jarFile = URLUtil.getJarFile(url);
            fileOutConfigList = FileOutConfigUtils.resolveByJarFile(autoGenerator, configParameter, jarFile);
        }
        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByFileSystem(AutoGenerator autoGenerator, ConfigParameter configParameter) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        List<File> fileList = FileUtil.loopFiles(configParameter.getTemplateLocation(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(configParameter.getTemplateType().name()));

        if (CollectionUtil.isEmpty(fileList)) {
            throw new RuntimeException("No template files found in location " + configParameter.getTemplateLocation());
        }
        log.info("Found {} template files in location {}.", fileList.size(), configParameter.getTemplateLocation());

        fileList.forEach(file -> {
            // 自定义配置会被优先输出
            FileOutConfig fileOutConfig = new FileOutConfigImpl(autoGenerator, configParameter.getTemplateLocation() + file.getName());
            fileOutConfigList.add(fileOutConfig);
        });

        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByJarFile(AutoGenerator autoGenerator, ConfigParameter configParameter, JarFile jarFile) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        InputStream inputStream = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<JarEntry> enumeration = jarFile.entries();
            String jarEntryName;
            while (enumeration.hasMoreElements()) {
                jarEntryName = enumeration.nextElement().getName();
                if (jarEntryName.startsWith(configParameter.getTemplateLocation()) && !jarEntryName.endsWith("/")) {
                    inputStream = classLoader.getResource(jarEntryName).openConnection().getInputStream();

                    FileOutConfig fileOutConfig = new FileOutConfigImpl(autoGenerator, jarEntryName);
                    fileOutConfigList.add(fileOutConfig);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(inputStream);
        }

        return fileOutConfigList;
    }
}
