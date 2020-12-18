package com.github.mengweijin.generator.mojo;

import cn.hutool.core.bean.BeanUtil;
import com.github.mengweijin.dto.ConfigParameter;
import com.github.mengweijin.dto.GeneratorConfig;
import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;

/**
 * @author mengweijin
 */
@Getter
public abstract class AbstractGeneratorMojo extends AbstractMojo {

    @Parameter
    private ConfigParameter configParameter;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDir;

    @Parameter(defaultValue = "${project.build.testResources}", readonly = true, required = true)
    private List<Resource> testResources;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
    private File testSourceDir;

    @Parameter(defaultValue = "${project.compileClasspathElements}", readonly = true, required = true)
    private List<String> compilePath;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    protected GeneratorConfig getGeneratorConfig() {
        this.configParameter = Optional.ofNullable(this.configParameter).orElse(new ConfigParameter());
        GeneratorConfig generatorConfig = BeanUtil.copyProperties(configParameter, GeneratorConfig.class);
        generatorConfig.setClassLoader(this.getClassLoader());
        generatorConfig.setMavenSession(this.getSession());
        generatorConfig.setMavenProject(this.getProject());
        generatorConfig.setResourceList(this.getResources());
        generatorConfig.setBaseDir(this.baseDir);
        generatorConfig.setSourceDir(this.sourceDir);
        return generatorConfig;
    }

    /**
     * getClassLoader(this.project).loadClass("com.somepackage.SomeClass")
     * project.getArtifact().getFile().getAbsolutePath()
     *
     * @return ClassLoader
     */
    protected ClassLoader getClassLoader() {
        try {
            // 所有的类路径环境，也可以直接用 compilePath
            List<String> classpathElements = project.getCompileClasspathElements();

            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            // 转为 URL 数组
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            // 自定义类加载器
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            getLog().warn("Couldn't get the classloader.");
            return this.getClass().getClassLoader();
        }
    }

}
