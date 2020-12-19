package com.github.mengweijin.generator.reader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.github.mengweijin.dto.DbInfo;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

public class PropertiesBootFileReader implements BootFileReader {

    @Override
    public String getActiveProfilesEnv(File file) {
        try {
            Props props = new Props(file.toURI().toURL(), StandardCharsets.UTF_8);
            return props.getStr(SPRING_PROFILES_ACTIVE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public DbInfo getDbInfo(File file) {
        DbInfo dbInfo = new DbInfo();
        try {
            Props props = new Props(file.toURI().toURL(), StandardCharsets.UTF_8);
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
