package com.github.mengweijin.generator.mojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.JarClassLoader;
import com.github.mengweijin.generator.dto.ConfigParameter;
import com.github.mengweijin.generator.dto.DefaultConfigParameter;
import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Method;
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

    protected DefaultConfigParameter getGeneratorConfig() {
        this.loadParentProjectClassToSystemClassLoader();
        this.configParameter = Optional.ofNullable(this.configParameter).orElse(new ConfigParameter());
        DefaultConfigParameter defaultConfigParameter = BeanUtil.copyProperties(configParameter, DefaultConfigParameter.class);
        defaultConfigParameter.setMavenSession(this.getSession());
        defaultConfigParameter.setMavenProject(this.getProject());
        defaultConfigParameter.setResourceList(this.getResources());
        defaultConfigParameter.setBaseDir(this.baseDir);
        defaultConfigParameter.setSourceDir(this.sourceDir);
        return defaultConfigParameter;
    }

    protected void loadParentProjectClassToSystemClassLoader() {
        URLClassLoader urlLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);

            List<String> classpathElements = project.getCompileClasspathElements();
            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
                method.invoke(urlLoader, urls[i]);
            }
        } catch (Exception e) {
            getLog().error("Load Parent Project Class to ClassLoader Error.");
            throw new RuntimeException(e);
        }
    }

    protected void loadParentProjectClassToJarClassLoader() {
        try {
            List<String> classpathElements = project.getCompileClasspathElements();

            URL[] urls = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }
            JarClassLoader jarClassLoader = new JarClassLoader(urls);
            Thread.currentThread().setContextClassLoader(jarClassLoader);

        } catch (Exception e) {
            getLog().error("Load Parent Project Class to ClassLoader Error.");
            throw new RuntimeException(e);
        }
    }
}
