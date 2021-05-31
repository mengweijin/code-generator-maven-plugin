package com.github.mengweijin.generator.mojo;

import cn.hutool.core.io.FileUtil;
import com.github.mengweijin.generator.engine.BeetlStringTemplateEngine;
import com.github.mengweijin.generator.entity.Docker;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.util.TemplateUtils;
import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释说明：@Execute(phase = LifecyclePhase.PACKAGE) 在执行当前 Mojo 前先打包
 * @author mengweijin
 */
@Getter
@Mojo(name = "Dockerfile")
@Execute(phase = LifecyclePhase.PACKAGE)
public class DockerfileGeneratorMojo extends AbstractMojo {

    @Parameter
    private Docker docker;

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            // clean TMP folder
            FileUtil.del(FileUtil.file(ProjectInfo.TMP_DIR));
            TemplateUtils.copyTemplateFolderToJavaTmp("templates/");

            BeetlStringTemplateEngine templateEngine = new BeetlStringTemplateEngine();
            templateEngine.init(null);

            Map<String, Object> objectMap = new HashMap<>(2);
            objectMap.put("ARTIFACT_ID", project.getArtifactId());
            objectMap.put("VERSION", project.getVersion());

            File dockerfile = this.generateDockerfile(templateEngine, objectMap);

            objectMap.put("DockerFileDirectory", dockerfile.getParentFile().getAbsolutePath());
            this.generateDockerImageBuild(templateEngine, objectMap);
            this.generateDockerImageBuildRun(templateEngine, objectMap);
            this.generateDockerImageDelete(templateEngine, objectMap);
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

    public File generateDockerfile(BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        String templatePath = ProjectInfo.TMP_DIR + "templates/docker/Dockerfile.btl";
        String outputFilePath = baseDir + File.separator + "Dockerfile";
        return render(templatePath, outputFilePath, templateEngine, objectMap);
    }

    public File generateDockerImageBuild(BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        String templatePath = ProjectInfo.TMP_DIR + "templates/docker/DockerImageBuild.btl";
        String outputFilePath = baseDir + File.separator + "DockerImageBuild.bat";
        return render(templatePath, outputFilePath, templateEngine, objectMap);
    }

    public File generateDockerImageBuildRun(BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        String templatePath = ProjectInfo.TMP_DIR + "templates/docker/DockerImageBuildRun.btl";
        String outputFilePath = baseDir + File.separator + "DockerImageBuildRun.bat";
        return render(templatePath, outputFilePath, templateEngine, objectMap);
    }

    public File generateDockerImageDelete(BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        String templatePath = ProjectInfo.TMP_DIR + "templates/docker/DockerImageDelete.btl";
        String outputFilePath = baseDir + File.separator + "DockerImageDelete.bat";
        return render(templatePath, outputFilePath, templateEngine, objectMap);
    }

    private File render(String templatePath, String outputFilePath, BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        File outputFile = FileUtil.file(outputFilePath);
        FileUtil.mkParentDirs(outputFile);
        templateEngine.writer(objectMap, templatePath, outputFile);
        return outputFile;
    }

}
