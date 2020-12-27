package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.CodeGenerator;
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
            CodeGenerator codeGenerator = this.getCodeGenerator();
            Parameters parameters = codeGenerator.getParameters();

            String templateLocation = CodeGenerator.TMP_DIR + Template.MYBATIS.getPath();
            parameters.setTemplateLocation(templateLocation);

            parameters.setTemplateType(TemplateType.beetl);

            codeGenerator.run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
