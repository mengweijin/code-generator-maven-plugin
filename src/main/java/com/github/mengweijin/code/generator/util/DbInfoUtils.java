package com.github.mengweijin.code.generator.util;

import com.alibaba.fastjson2.JSON;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.dto.DbInfo;
import com.github.mengweijin.code.generator.reader.BootFileReaderFactory;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileNameUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
public class DbInfoUtils {

    private static final String[] BOOTSTRAP_FILE = {
            "bootstrap.yml",
            "bootstrap.yaml",
            "bootstrap.properties"
    };

    private static final String[] APPLICATION_FILE = {
            "application.yml",
            "application.yaml",
            "application.properties"
    };

    private static final String APPLICATION_CONFIG_FILE_REGEX = "^((application)|(bootstrap))((-\\S*)?)\\.((yaml)|(yml)|(properties))$";

    public static final String SRC_TEST_JAVA = "src/test/java/";
    public static final String SRC_MAIN_JAVA = "src/main/java/";

    /**
     * Initialize the default parameter.
     */
    public static DbInfo getDbInfo(Config config, Set<String> allProjectRuntimeClasspathElements) {
        DbInfo dbInfo = config.getDbInfo();
        if (dbInfo == null || StrUtil.isBlank(dbInfo.getUrl())) {
            dbInfo = generateDefaultDbInfo(allProjectRuntimeClasspathElements);
        }

        return dbInfo;
    }

    private static DbInfo generateDefaultDbInfo(Set<String> allProjectRuntimeClasspathElements) {
        List<String> classesList = allProjectRuntimeClasspathElements.stream().filter(el -> el.endsWith("\\classes")).collect(Collectors.toList());
        // classes: D:\code\vitality\vitality-admin\target\classes
        File applicationFile = null;
        for (String classes : classesList) {
            applicationFile = getBootFile(classes);
            if(applicationFile != null) {
                break;
            }
        }
        if (applicationFile == null) {
           throw new RuntimeException("Can't find any file " + JSON.toJSONString(APPLICATION_FILE));
        }
        String[] activeProfilesEnv = BootFileReaderFactory.getActiveProfiles(applicationFile);
        DbInfo dbInfo = null;
        if(activeProfilesEnv != null) {
            for (String env : activeProfilesEnv) {
                String activeBootFilePath = applicationFile.getParent() + File.separator + "application-" + env + StrUtil.DOT + FileNameUtil.getSuffix(applicationFile);
                dbInfo = BootFileReaderFactory.getDbInfo(FileUtil.file(activeBootFilePath));
                if(dbInfo != null) {
                    break;
                }
            }
        }
        if(dbInfo == null) {
           dbInfo = BootFileReaderFactory.getDbInfo(applicationFile);
        }
        return dbInfo;
    }

    private static File getBootFile(String resources) {
        List<File> fileList = FileUtil.loopFiles(FileUtil.file(resources), 1, file -> {
            for (String fileName : DbInfoUtils.APPLICATION_FILE) {
                if (fileName.equals(file.getName())) {
                    return true;
                }
            }
            return false;
        });
        return CollUtil.isEmpty(fileList) ? null : fileList.get(0);
    }
}
