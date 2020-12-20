package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.dto.DefaultConfigParameter;
import com.github.mengweijin.generator.enums.Template;
import com.github.mengweijin.generator.main.CodeGenerator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "jpa", requiresDependencyResolution = ResolutionScope.COMPILE)
public class JpaGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            DefaultConfigParameter parameter = this.getGeneratorConfig();
            parameter.initDefaultValue();
            parameter.setTemplateLocation(Template.JPA.getPath());
            parameter.setSuperDaoClass("org.springframework.data.jpa.repository.JpaRepository");

            System.out.println("DefaultConfigParameter: " + parameter);

            new CodeGenerator(parameter).run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
