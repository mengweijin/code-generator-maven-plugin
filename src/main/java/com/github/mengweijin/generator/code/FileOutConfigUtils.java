package com.github.mengweijin.generator.code;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
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
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
@Slf4j
public class FileOutConfigUtils {

    public static List<FileOutConfig> loadTemplatesToGetFileOutConfig(ConfigProperty config, AutoGenerator autoGenerator) {
        // 自定义输出配置
        List<FileOutConfig> fileOutConfigList = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource(config.getTemplate());
        if (URLUtil.isFileURL(url)) {
            fileOutConfigList = FileOutConfigUtils.resolveByFileSystem(autoGenerator, config);
        } else if (URLUtil.isJarURL(url)) {
            JarFile jarFile = URLUtil.getJarFile(url);
            fileOutConfigList = FileOutConfigUtils.resolveByJarFile(autoGenerator, config, jarFile);
        }
        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByFileSystem(AutoGenerator autoGenerator, ConfigProperty config) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        List<File> fileList = FileUtil.loopFiles(config.getTemplate(),
                file -> file.isFile() && file.getName().toLowerCase().endsWith(config.getTemplateSuffix()));

        if (CollectionUtil.isEmpty(fileList)) {
            throw new RuntimeException("No template files found in location " + config.getTemplate());
        }
        log.info("Found {} template files in location {}.", fileList.size(), config.getTemplate());

        fileList.forEach(file -> {
            // 自定义配置会被优先输出
            FileOutConfig fileOutConfig = new FileOutConfigImpl(autoGenerator, config.getTemplate() + file.getName());
            fileOutConfigList.add(fileOutConfig);
        });

        return fileOutConfigList;
    }

    private static List<FileOutConfig> resolveByJarFile(AutoGenerator autoGenerator, ConfigProperty config, JarFile jarFile) {
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();

        InputStream inputStream = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<JarEntry> enumeration = jarFile.entries();
            String jarEntryName;
            while (enumeration.hasMoreElements()) {
                jarEntryName = enumeration.nextElement().getName();
                if (jarEntryName.startsWith(config.getTemplate()) && !jarEntryName.endsWith("/")) {
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

    public static boolean checkNotContainExistsFiles(List<FileOutConfig> fileOutConfigList) {
        List<String> collect = fileOutConfigList.stream().filter(fileOutConfig -> {
            FileOutConfigImpl impl = (FileOutConfigImpl) fileOutConfig;
            File file = FileUtil.file(impl.getOutputFile());
            return file.exists();
        }).map(fileOutConfig -> {
            FileOutConfigImpl impl = (FileOutConfigImpl) fileOutConfig;
            return impl.getOutputFile();
        }).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(collect)) {
            return true;
        } else {
            collect.forEach(file -> log.error("File already exists. {}", file));
            return false;
        }
    }
}
