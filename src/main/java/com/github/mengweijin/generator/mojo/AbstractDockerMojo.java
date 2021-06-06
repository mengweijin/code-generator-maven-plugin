package com.github.mengweijin.generator.mojo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.github.mengweijin.generator.engine.BeetlStringTemplateEngine;
import com.github.mengweijin.generator.entity.Docker;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.util.TemplateUtils;
import lombok.Getter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 注释说明：@Execute(phase = LifecyclePhase.PACKAGE) 在执行当前 Mojo 前先打包
 * @author mengweijin
 */
@Getter
public abstract class AbstractDockerMojo extends AbstractMojo {

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

    protected void generate() throws Exception {
        // clean TMP folder
        FileUtil.del(FileUtil.file(ProjectInfo.TMP_DIR));
        TemplateUtils.copyTemplateFolderToJavaTmp("templates/");

        Map<String, Object> objectMap = new HashMap<>(2);
        objectMap.put("ARTIFACT_ID", project.getArtifactId());
        objectMap.put("VERSION", project.getVersion());

        BeetlStringTemplateEngine templateEngine = new BeetlStringTemplateEngine();
        templateEngine.init(null);

        this.generateDockerScript(templateEngine, objectMap);
    }

    protected void execBash(String bashName) throws Exception {
        this.generate();

        // return C:
        String currentFileSystemMount = StrUtil.subPre(this.dockerfileDirector(), 2).toUpperCase(Locale.ROOT);

        StringBuilder builder =
                new StringBuilder("cmd /c ").append(currentFileSystemMount).append(" && ")
                        .append("cd ").append(this.dockerfileDirector()).append(" && ")
                        .append(bashName);

        String cmdOutput = RuntimeUtil.execForStr(builder.toString());
        getLog().info(cmdOutput);
    }

    private void generateDockerScript(BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        String templatePath = ProjectInfo.TMP_DIR + "templates/docker";
        List<File> templateFileList = FileUtil.loopFiles(templatePath,
                file -> file.isFile() && file.getName().toLowerCase().endsWith(".btl"));

        String outputFilePath;
        for (File templateFile: templateFileList) {
            outputFilePath = dockerfileDirector() + StrUtil.subBefore(templateFile.getName(), StrUtil.DOT, true);
            getLog().info("Template " + templateFile.getAbsolutePath() + "--->" + outputFilePath);
            render(templateFile.getAbsolutePath(), outputFilePath, templateEngine, objectMap);
        }
    }

    protected String dockerfileDirector() {
        return baseDir.getAbsolutePath() + File.separator + "target" + File.separator;
    }

    private File render(String templatePath, String outputFilePath, BeetlStringTemplateEngine templateEngine, Map<String, Object> objectMap) throws Exception {
        File outputFile = FileUtil.file(outputFilePath);
        FileUtil.mkParentDirs(outputFile);
        templateEngine.writer(objectMap, templatePath, outputFile);
        return outputFile;
    }

}
