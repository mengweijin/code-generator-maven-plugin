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
            DefaultConfigParameter defaultConfigParameter = this.getGeneratorConfig();
            defaultConfigParameter.initDefaultValue();
            defaultConfigParameter.setTemplateLocation(Template.JPA.getPath());
            System.out.println("DefaultConfigParameter: " + defaultConfigParameter);

            new CodeGenerator(defaultConfigParameter).run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
