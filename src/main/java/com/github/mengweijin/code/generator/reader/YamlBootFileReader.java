package com.github.mengweijin.code.generator.reader;

import com.alibaba.fastjson2.JSONPath;
import com.github.mengweijin.code.generator.dto.DbInfo;
import com.github.mengweijin.code.generator.util.YamlUtils;
import org.dromara.hutool.core.text.StrUtil;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author mengweijin
 */
public class YamlBootFileReader implements BootFileReader {
    @Override
    public String[] getActiveProfiles(File file) {
        LinkedHashMap map = YamlUtils.loadAs(file, LinkedHashMap.class);
        Object object = JSONPath.eval(map, "$.spring.profiles.active");
        String activeProfiles = StrUtil.cleanBlank(StrUtil.toStringOrNull(object));
        if(StrUtil.isBlank(activeProfiles)) {
            return null;
        }
        return activeProfiles.split(",");
    }

    @Override
    public DbInfo getDbInfo(File file) {
        LinkedHashMap map = YamlUtils.loadAs(file, LinkedHashMap.class);
        Object datasource = JSONPath.eval(map, "$.spring.datasource");
        if(datasource == null) {
            return null;
        }

        Object url = JSONPath.eval(map, "$.spring.datasource.url");
        Object username = JSONPath.eval(map, "$.spring.datasource.username");
        Object password = JSONPath.eval(map, "$.spring.datasource.password");

        DbInfo dbInfo = new DbInfo();
        dbInfo.setUrl(url == null ? null : String.valueOf(url));
        dbInfo.setUsername(username == null ? null : String.valueOf(username));
        dbInfo.setPassword(password == null ? null : String.valueOf(password));
        return dbInfo;
    }
}
