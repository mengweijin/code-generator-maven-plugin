package com.github.mengweijin.generator.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.github.mengweijin.generator.enums.TemplateType;
import com.github.mengweijin.generator.reader.BootFileReaderFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author mengweijin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultConfigParameter extends ConfigParameter {

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

    private MavenSession mavenSession;

    private MavenProject mavenProject;

    private List<Resource> resourceList;

    private File baseDir;

    private File sourceDir;

    public DefaultConfigParameter initDefaultValue() {
        this.setAuthor(Optional.ofNullable(this.getAuthor()).orElse(SystemUtil.getUserInfo().getName()));
        this.setTemplateType(Optional.ofNullable(this.getTemplateType()).orElse(TemplateType.beetl));

        String outputPath;
        if(StrUtil.isBlank(this.getOutputPackage())) {
            outputPath = SRC_TEST_JAVA + "com.github.mengweijin.generator";
        } else {
            outputPath = SRC_MAIN_JAVA + this.getOutputPackage();
        }
        this.setOutputPackage(StrUtil.replace(outputPath, StrUtil.SLASH, StrUtil.DOT));

        if (this.getSuperEntityClass() != null && this.getSuperEntityColumns() == null) {
            this.setSuperEntityColumns(this.generateDefaultSuperEntityColumns());
        }

        // url/driverName/username/password
        DbInfo dbInfo = this.getDbInfo();
        if (dbInfo == null || StrUtil.isBlank(dbInfo.getUrl())) {
            this.setDbInfo(this.generateDefaultDbInfo());
        }

        return this;
    }

    /**
     * If the user configured superEntityColumns, the configuration will prevail;
     * if not, the default configuration of superEntityColumns will be generated according to the superEntityClass.
     *
     * @return String[]
     */
    private String[] generateDefaultSuperEntityColumns() {
        try {
            ClassLoader classLoader = ClassLoaderUtil.getClassLoader();
            Class<?> cls = Class.forName(this.getSuperEntityClass(), true, classLoader);
            Field[] declaredFields = ClassUtil.getDeclaredFields(cls);
            return Arrays.stream(declaredFields)
                    .map(field -> StrUtil.toUnderlineCase(field.getName())).toArray(String[]::new);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private DbInfo generateDefaultDbInfo() {
        Resource resource = resourceList.stream().filter(res -> res.getDirectory().endsWith("\\resources")).findFirst().get();
        //File bootstrapFile = this.getBootFile(resource, BOOTSTRAP_FILE);

        File applicationFile = this.getBootFile(resource, APPLICATION_FILE);
        if (applicationFile == null) {
            throw new RuntimeException("Can't find any file " + JSON.toJSONString(APPLICATION_FILE));
        }
        String activeProfilesEnv = BootFileReaderFactory.getActiveProfilesEnv(applicationFile);
        DbInfo dbInfo;
        if (StrUtil.isBlank(activeProfilesEnv)) {
            dbInfo = BootFileReaderFactory.getDbInfo(applicationFile);
        } else {
            String activeBootFilePath = resource.getDirectory() + File.separator +
                    "application-" + activeProfilesEnv + StrUtil.DOT + FileNameUtil.getSuffix(applicationFile);
            File activeBootFile = FileUtil.file(activeBootFilePath);
            dbInfo = BootFileReaderFactory.getDbInfo(activeBootFile);
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

    @Override
    public String toString() {
        return "ConfigParameter(author=" + this.getAuthor() +
                ", templateLocation=" + this.getTemplateLocation() +
                ", templateType=" + this.getTemplateType() +
                ", outputPath=" + this.getOutputPackage() +
                ", tables=" + Arrays.deepToString(this.getTables()) +
                ", tablePrefix=" + Arrays.deepToString(this.getTablePrefix()) +
                ", superEntityClass=" + this.getSuperEntityClass() +
                ", superDaoClass=" + this.getSuperDaoClass() +
                ", superServiceClass=" + this.getSuperServiceClass() +
                ", superServiceImplClass=" + this.getSuperServiceImplClass() +
                ", superControllerClass=" + this.getSuperControllerClass() +
                ", superEntityColumns=" + Arrays.deepToString(this.getSuperEntityColumns()) +
                ", dbInfo=" + this.getDbInfo() + ")";
    }
}
