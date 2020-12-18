package com.github.mengweijin.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.github.mengweijin.generator.enums.TemplateType;
import com.github.mengweijin.generator.util.BootFileReader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class GeneratorConfig extends ConfigParameter {

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

    private ClassLoader classLoader;

    private MavenSession mavenSession;

    private MavenProject mavenProject;

    private List<Resource> resourceList;

    private File baseDir;

    private File sourceDir;

    public GeneratorConfig initDefaultValue() {
        this.setAuthor(Optional.ofNullable(this.getAuthor()).orElse(SystemUtil.getUserInfo().getName()));
        this.setTemplateType(Optional.ofNullable(this.getTemplateType()).orElse(TemplateType.beetl));
        this.setOutputPackage(Optional.ofNullable(this.getOutputPackage()).orElse("target/code-generator/"));

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
            Class<?> cls = classLoader.loadClass(this.getSuperEntityClass());
            Field[] declaredFields = ClassUtil.getDeclaredFields(cls);
            return Arrays.stream(declaredFields)
                    .map(field -> StrUtil.toUnderlineCase(field.getName())).toArray(String[]::new);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private DbInfo generateDefaultDbInfo() {
        Resource resource = resourceList.stream().filter(res -> res.getDirectory().endsWith("\\resources")).findFirst().get();
        // The latter field overrides the previous field. For example,
        // if the same configuration is found in both application.yml and bootstrap.yml, the configuration in bootstrap is preferred.
        File applicationFile = this.getBootFile(resource, APPLICATION_FILE);
        File bootstrapFile = this.getBootFile(resource, BOOTSTRAP_FILE);
        String activeProfilesEnv = BootFileReader.getActiveProfilesEnv(applicationFile, bootstrapFile);


        return null;
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
}
