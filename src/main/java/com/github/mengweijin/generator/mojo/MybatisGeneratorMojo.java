package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.ProjectInfo;
import com.github.mengweijin.generator.Parameters;
import com.github.mengweijin.generator.enums.Template;
import com.github.mengweijin.generator.enums.TemplateType;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "mybatis", requiresDependencyResolution = ResolutionScope.COMPILE)
public class MybatisGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            ProjectInfo projectInfo = this.getProjectInfo();
            Parameters parameters = projectInfo.getParameters();
            parameters.setTemplateLocation(ProjectInfo.TMP_DIR + Template.MYBATIS.getPath());
            parameters.setTemplateType(TemplateType.beetl);

            new CodeGenerator(projectInfo).run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
