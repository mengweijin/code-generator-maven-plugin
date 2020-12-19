package com.github.mengweijin.generator.reader;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.EnumUtil;
import com.github.mengweijin.generator.dto.DbInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mengweijin
 */
@Slf4j
public class BootFileReaderFactory {

    private static final Map<BootFileType, Class> map = new HashMap<>();

    static {
        map.put(BootFileType.yml, YamlBootFileReader.class);
        map.put(BootFileType.yaml, YamlBootFileReader.class);
        map.put(BootFileType.properties, PropertiesBootFileReader.class);
    }

    public static String getActiveProfilesEnv(File file) {
        return getBootFileReader(file).getActiveProfilesEnv(file);
    }

    public static DbInfo getDbInfo(File file) {
        return getBootFileReader(file).getDbInfo(file);
    }

    private static BootFileReader getBootFileReader(File file) {
        try {
            String suffix = FileNameUtil.getSuffix(file);
            BootFileType bootFileType = EnumUtil.fromString(BootFileType.class, suffix);
            Class cls = map.get(bootFileType);
            return (BootFileReader) cls.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
