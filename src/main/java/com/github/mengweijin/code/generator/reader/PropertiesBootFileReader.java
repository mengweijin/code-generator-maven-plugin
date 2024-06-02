package com.github.mengweijin.code.generator.reader;

import com.github.mengweijin.code.generator.dto.DbInfo;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.setting.props.Props;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class PropertiesBootFileReader implements BootFileReader {

    @Override
    public String[] getActiveProfiles(File file) {
        Props props = new Props(file, StandardCharsets.UTF_8);
        String activeProfiles = props.getStr(SPRING_PROFILES_ACTIVE);
        activeProfiles = StrUtil.cleanBlank(activeProfiles);
        if(StrUtil.isBlank(activeProfiles)) {
            return null;
        }
        return activeProfiles.split(",");
    }

    @Override
    public DbInfo getDbInfo(File file) {
        Props props = new Props(file, StandardCharsets.UTF_8);
        String url = props.getStr(SPRING_DATASOURCE_URL);
        if(StrUtil.isBlank(url)) {
            return null;
        }

        DbInfo dbInfo = new DbInfo();
        dbInfo.setUrl(url);
        dbInfo.setUsername(props.getStr(SPRING_DATASOURCE_USERNAME));
        dbInfo.setPassword(props.getStr(SPRING_DATASOURCE_PASSWORD));
        return dbInfo;
    }
}
