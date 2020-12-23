package com.github.mengweijin.generator.mojo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.JarClassLoader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.Parameters;
import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author mengweijin
 */
@Getter
public abstract class AbstractGeneratorMojo extends AbstractMojo {

    @Parameter
    private Parameters parameters;

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

    protected CodeGenerator getCodeGenerator() {
        this.loadParentProjectClassToJarClassLoader();
        this.copyTemplateFolderToJavaTmp("templates/");
        this.parameters = Optional.ofNullable(this.parameters).orElse(new Parameters());
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.setParameters(this.parameters);
        codeGenerator.setResourceList(this.getResources());
        codeGenerator.setBaseDir(this.baseDir);
        codeGenerator.setSourceDir(this.sourceDir);
        return codeGenerator;
    }

    protected void loadParentProjectClassToApplicationClassLoader() {
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

    /**
     *
     * @param classPathResource "templates/"
     */
    private void copyTemplateFolderToJavaTmp(String classPathResource) {
        File tmpFile;
        JarFile jarFile = null;
        InputStream inputStream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(classPathResource);
        try {
            if (URLUtil.isJarURL(url)) {
                jarFile = URLUtil.getJarFile(url);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                String jarEntryName;
                while (enumeration.hasMoreElements()) {
                    jarEntryName = enumeration.nextElement().getName();
                    if (jarEntryName.startsWith(classPathResource) && !jarEntryName.endsWith(StrUtil.SLASH)) {
                        inputStream = classLoader.getResource(jarEntryName).openConnection().getInputStream();
                        tmpFile = FileUtil.file(CodeGenerator.TMP_DIR + jarEntryName);
                        FileUtil.writeFromStream(inputStream, tmpFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(jarFile);
        }
    }
}
