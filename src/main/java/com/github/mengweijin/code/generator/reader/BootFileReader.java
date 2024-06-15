package com.github.mengweijin.code.generator.reader;

import com.github.mengweijin.code.generator.dto.DbInfo;

import java.io.File;

/**
 * @author mengweijin
 */
public interface BootFileReader {

    String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    String SPRING_DATASOURCE_DRIVERCLASSNAME = "spring.datasource.driverClassName";
    String SPRING_DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    String SPRING_DATASOURCE_URL = "spring.datasource.url";
    String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    /**
     * get active profiles env
     *
     * @param file file
     * @return active profiles env
     */
    String[] getActiveProfiles(File file);

    /**
     * get db info
     *
     * @param file file
     * @return DbInfo
     */
    DbInfo getDbInfo(File file);
}
