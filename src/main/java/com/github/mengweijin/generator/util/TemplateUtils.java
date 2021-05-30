package com.github.mengweijin.generator.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.github.mengweijin.generator.entity.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author mengweijin
 */
@Slf4j
public final class TemplateUtils {
    /**
     *
     * @param classPathResource "templates/"
     */
    public static void copyTemplateFolderToJavaTmp(String classPathResource) {
        File tmpFile;
        JarFile jarFile = null;
        InputStream inputStream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(classPathResource);
        try {
            if (URLUtil.isJarURL(url)) {
                jarFile = URLUtil.getJarFile(url);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                String jarEntryName;
                while (enumeration.hasMoreElements()) {
                    jarEntryName = enumeration.nextElement().getName();
                    if (jarEntryName.startsWith(classPathResource) && !jarEntryName.endsWith(StrUtil.SLASH)) {
                        inputStream = classLoader.getResource(jarEntryName).openConnection().getInputStream();
                        tmpFile = FileUtil.file(ProjectInfo.TMP_DIR + jarEntryName);
                        FileUtil.writeFromStream(inputStream, tmpFile);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(jarFile);
        }
    }
}
