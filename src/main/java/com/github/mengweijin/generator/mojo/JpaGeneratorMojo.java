package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.dto.DefaultConfigParameter;
import com.github.mengweijin.generator.enums.Template;
import com.github.mengweijin.generator.main.CodeGenerator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author mengweijin
 */
@Mojo(name = "jpa", threadSafe = true)
public class JpaGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        DefaultConfigParameter defaultConfigParameter = this.getGeneratorConfig();
        defaultConfigParameter.initDefaultValue();
        defaultConfigParameter.setTemplateLocation(Template.JPA.getPath());

        new CodeGenerator(defaultConfigParameter).run();
    }


}
