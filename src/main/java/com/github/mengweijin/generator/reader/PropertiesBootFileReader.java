package com.github.mengweijin.generator.reader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.github.mengweijin.generator.DbInfo;

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
        try {
            Props props = new Props(file.toURI().toURL(), StandardCharsets.UTF_8);
            String url = props.getStr(SPRING_DATASOURCE_URL);
            if(StrUtil.isBlank(url)) {
                return null;
            }

            DbInfo dbInfo = new DbInfo();
            dbInfo.setUrl(url);
            String driverName = props.getStr(SPRING_DATASOURCE_DRIVERCLASSNAME);
            if (StrUtil.isBlank(driverName)) {
                driverName = props.getStr(SPRING_DATASOURCE_DRIVER_CLASS_NAME);
            }
            dbInfo.setDriverName(driverName);
            dbInfo.setUsername(props.getStr(SPRING_DATASOURCE_USERNAME));
            dbInfo.setPassword(props.getStr(SPRING_DATASOURCE_PASSWORD));

            return dbInfo;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
