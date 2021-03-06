package com.github.mengweijin.generator.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.DbInfo;
import com.github.mengweijin.generator.reader.BootFileReaderFactory;
import org.apache.maven.model.Resource;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

/**
 * @author mengweijin
 */
public class DefaultDataSourceConfig extends DataSourceConfig {

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


    private CodeGenerator codeGenerator;

    public DefaultDataSourceConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.init();
    }

    /**
     * Initialize the default parameter.
     */
    public void init() {
        DbInfo dbInfo = codeGenerator.getParameters().getDbInfo();
        if (dbInfo == null || StrUtil.isBlank(dbInfo.getUrl())) {
            dbInfo = this.generateDefaultDbInfo();
        }
        this.setUrl(dbInfo.getUrl());
        this.setDriverName(dbInfo.getDriverName());
        this.setUsername(dbInfo.getUsername());
        this.setPassword(dbInfo.getPassword());
    }

    private DbInfo generateDefaultDbInfo() {
        List<Resource> resourceList = codeGenerator.getResourceList();
        Resource resource = resourceList.stream().filter(res -> res.getDirectory().endsWith("\\resources")).findFirst().get();

        File applicationFile = this.getBootFile(resource, APPLICATION_FILE);
        if (applicationFile == null) {
            throw new RuntimeException("Can't find any file " + JSON.toJSONString(APPLICATION_FILE));
        }
        String activeProfilesEnv = BootFileReaderFactory.getActiveProfilesEnv(applicationFile);
        DbInfo dbInfo = null;
        if(StrUtil.isNotBlank(activeProfilesEnv)) {
            String activeBootFilePath = resource.getDirectory() + File.separator +
                    "application-" + activeProfilesEnv + StrUtil.DOT + FileNameUtil.getSuffix(applicationFile);
            File activeBootFile = FileUtil.file(activeBootFilePath);
            dbInfo = BootFileReaderFactory.getDbInfo(activeBootFile);
        }

        if(dbInfo == null) {
            dbInfo = BootFileReaderFactory.getDbInfo(applicationFile);
        }

        return dbInfo;
    }

    private File getBootFile(Resource resource, String[] filterNames) {
        File resourcesDir = FileUtil.file(resource.getDirectory());
        List<File> fileList = FileUtil.loopFiles(resourcesDir, 1, file -> {
            for (String fileName : filterNames) {
                if (fileName.equals(file.getName())) {
                    return true;
                }
            }
            return false;
        });

        return CollectionUtil.isEmpty(fileList) ? null : fileList.get(0);
    }

    /**
     * Override the parent class method
     *
     * @return Connection
     */
    @Override
    public Connection getConn() {
        Connection conn;
        try {
            Class.forName(this.getDriverName(), true, Thread.currentThread().getContextClassLoader());

            Properties info = new Properties();
            if (this.getUsername() != null) {
                info.put("user", this.getUsername());
            }
            if (this.getPassword() != null) {
                info.put("password", this.getPassword());
            }

            Method method = ReflectUtil.getMethod(
                    DriverManager.class,
                    "getConnection",
                    String.class, Properties.class, Class.class);
            method.setAccessible(true);
            conn = ReflectUtil.invokeStatic(method, this.getUrl(), info, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
