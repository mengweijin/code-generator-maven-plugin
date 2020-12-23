package com.github.mengweijin.generator.reader;

import com.alibaba.fastjson.JSONPath;
import com.github.mengweijin.generator.DbInfo;
import com.github.mengweijin.generator.util.YamlUtils;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author mengweijin
 */
public class YamlBootFileReader implements BootFileReader {
    @Override
    public String getActiveProfilesEnv(File file) {
        LinkedHashMap map = YamlUtils.loadAs(file, LinkedHashMap.class);
        Object object = JSONPath.eval(map, "$." + SPRING_PROFILES_ACTIVE);
        return object == null ? null : String.valueOf(object);
    }

    @Override
    public DbInfo getDbInfo(File file) {
        DbInfo dbInfo = new DbInfo();
        LinkedHashMap map = YamlUtils.loadAs(file, LinkedHashMap.class);
        Object url = JSONPath.eval(map, "$." + SPRING_DATASOURCE_URL);
        Object driverName = JSONPath.eval(map, "$." + SPRING_DATASOURCE_DRIVERCLASSNAME);
        if (driverName == null) {
            // JSONPath中，中划线是特殊字符
            driverName = JSONPath.eval(map, "$.spring.datasource['driver-class-name']");
        }
        Object username = JSONPath.eval(map, "$." + SPRING_DATASOURCE_USERNAME);
        Object password = JSONPath.eval(map, "$." + SPRING_DATASOURCE_PASSWORD);

        dbInfo.setUrl(url == null ? null : String.valueOf(url));
        dbInfo.setDriverName(driverName == null ? null : String.valueOf(driverName));
        dbInfo.setUsername(username == null ? null : String.valueOf(username));
        dbInfo.setPassword(password == null ? null : String.valueOf(password));
        return dbInfo;
    }
}
