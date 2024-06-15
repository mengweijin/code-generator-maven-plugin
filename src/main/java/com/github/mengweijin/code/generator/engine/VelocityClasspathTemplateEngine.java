package com.github.mengweijin.code.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.enums.TemplateType;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.dromara.hutool.core.io.resource.ClassPathResource;
import org.dromara.hutool.core.io.resource.JarResource;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author mengweijin
 */
public class VelocityClasspathTemplateEngine extends AbstractVelocityTemplateEngine {

    /**
     * classpath
     * @param templateDir keep null value
     * @return VelocityEngine
     */
    @Override
    protected VelocityEngine getVelocityEngine(String templateDir) {
        Properties p = new Properties();
        p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
        p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
        p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
        p.setProperty("file.resource.loader.unicode", StringPool.TRUE);
        return new VelocityEngine(p);
    }

    @Override
    protected List<String> getTemplates() {
        Config config = this.getConfig();
        List<String> templateFilePathList = new ArrayList<>();

        ClassPathResource classPathResource = new ClassPathResource(config.getTemplateDir());
        JarResource jarResource = new JarResource(classPathResource.getUrl());
        JarFile jarFile = jarResource.getJarFile();
        Enumeration<JarEntry> enumeration = jarFile.entries();

        String templateLocationPrefixPath = config.getTemplateDir().endsWith("/") ? config.getTemplateDir() : config.getTemplateDir() + "/";
        while (enumeration.hasMoreElements()) {
            String jarEntryName = enumeration.nextElement().getName();
            if(jarEntryName.startsWith(templateLocationPrefixPath) && jarEntryName.endsWith(TemplateType.velocity.getSuffix())) {
                templateFilePathList.add(jarEntryName);
            }
        }
        return templateFilePathList;
    }
}
