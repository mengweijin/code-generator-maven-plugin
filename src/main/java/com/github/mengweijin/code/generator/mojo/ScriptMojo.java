package com.github.mengweijin.code.generator.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ClassPathResource;
import org.dromara.hutool.core.io.resource.JarResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 说明：@Execute(phase = LifecyclePhase.COMPILE) 在运行该目标前，让maven先运行一个并行的生命周期，到指定的阶段为止。到phase执行完，才执行插件目标
 * ResolutionScope.COMPILE_PLUS_RUNTIME:
 * Will add the classpath jars into list when call method project.getRuntimeClasspathElements();
 * maven scope=compile + system + provided + runtime dependencies
 *
 * @author mengweijin
 */
@Mojo(
        name = "script",
        aggregator = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class ScriptMojo extends AbstractMojo {

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    private static final String SCRIPT_DIR = "script/";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String baseOutputDirPath = baseDir.getAbsolutePath() + "/target/code-generator/";
        FileUtil.del(baseOutputDirPath);

        try {
            this.outputScripts(baseOutputDirPath);
        } catch (IOException e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

    private void outputScripts(String baseOutputDirPath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(SCRIPT_DIR);
        JarResource jarResource = new JarResource(classPathResource.getUrl());
        JarFile jarFile = jarResource.getJarFile();
        Enumeration<JarEntry> enumeration = jarFile.entries();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            String jarEntryName = jarEntry.getName();
            if(!jarEntry.isDirectory() && jarEntryName.startsWith(SCRIPT_DIR)) {
                InputStream inputStream = classLoader.getResource(jarEntryName).openConnection().getInputStream();
                File fileTargetDir = FileUtil.file(baseOutputDirPath + jarEntryName);
                FileUtil.mkParentDirs(fileTargetDir);
                FileUtil.copy(inputStream, fileTargetDir);
            }
        }
    }
}
