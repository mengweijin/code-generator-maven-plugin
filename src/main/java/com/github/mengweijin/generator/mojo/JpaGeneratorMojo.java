package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.enums.Template;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "jpa", requiresDependencyResolution = ResolutionScope.COMPILE)
public class JpaGeneratorMojo extends AbstractGeneratorMojo {

    private static final String JpaRepository = "org.springframework.data.jpa.repository.JpaRepository";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            CodeGenerator codeGenerator = this.getCodeGenerator();
            String templateLocation = CodeGenerator.TMP_DIR + Template.JPA.getPath();
            codeGenerator.getParameters().setTemplateLocation(templateLocation);
            codeGenerator.getParameters().setSuperDaoClass(JpaRepository);
            codeGenerator.run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
