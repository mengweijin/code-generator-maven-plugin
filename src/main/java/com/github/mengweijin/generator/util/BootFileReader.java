package com.github.mengweijin.generator.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.github.mengweijin.dto.DbInfo;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Slf4j
public class BootFileReader {

    private static final String YML = "yml";

    private static final String YAML = "yaml";

    private static final String PROPERTIES = "properties";

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String SPRING_DATASOURCE_DRIVERCLASSNAME = "spring.datasource.driverClassName";
    private static final String SPRING_DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    private static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    private static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    public static String getActiveProfilesEnv(File... files) {
        String activeProfiles;
        for (File file : files) {
            activeProfiles = getActiveProfilesEnv(file);
            if (activeProfiles != null) {
                return activeProfiles;
            }
        }
        return null;
    }

    private static String getActiveProfilesEnv(File file) {
        String activeProfiles = null;
        String suffix = FileNameUtil.getSuffix(file);
        if (PROPERTIES.equalsIgnoreCase(suffix)) {
            activeProfiles = getActiveProfilesEnvFromProperties(file);
        } else if (YML.equals(suffix) || YAML.equals(suffix)) {
            activeProfiles = getActiveProfilesEnvFromYml(file);
        } else {
            log.warn("Unsupported file type. " + file.getAbsolutePath());
        }
        return activeProfiles;
    }

    public static String getActiveProfilesEnvFromProperties(File file) {
        try {
            Props props = new Props(file.toURI().toURL(), StandardCharsets.UTF_8);
            return props.getStr(SPRING_PROFILES_ACTIVE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getActiveProfilesEnvFromYml(File file) {
        Yaml yaml = new Yaml();
        LinkedHashMap map = yaml.loadAs(FileUtil.getInputStream(file), LinkedHashMap.class);

        return null;
    }

    public static DbInfo getDbInfoFromProperties(File file) {
        DbInfo dbInfo = new DbInfo();
        try {
            Props props = new Props(file.toURI().toURL(), StandardCharsets.UTF_8);
            dbInfo.setActiveProfile(props.getStr(SPRING_PROFILES_ACTIVE));
            dbInfo.setUrl(props.getStr(SPRING_DATASOURCE_URL));
            String driverName = props.getStr(SPRING_DATASOURCE_DRIVERCLASSNAME);
            if (StrUtil.isBlank(driverName)) {
                driverName = props.getStr(SPRING_DATASOURCE_DRIVER_CLASS_NAME);
            }
            dbInfo.setDriverName(driverName);
            dbInfo.setUsername(props.getStr(SPRING_DATASOURCE_USERNAME));
            dbInfo.setPassword(props.getStr(SPRING_DATASOURCE_PASSWORD));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return dbInfo;
    }
}
