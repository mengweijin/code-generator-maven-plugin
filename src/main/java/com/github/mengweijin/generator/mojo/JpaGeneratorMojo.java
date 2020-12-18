package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.dto.GeneratorConfig;
import com.github.mengweijin.generator.code.CodeGenerator;
import com.github.mengweijin.generator.enums.Template;
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
        GeneratorConfig generatorConfig = this.getGeneratorConfig();
        generatorConfig.initDefaultValue();
        generatorConfig.setTemplateLocation(Template.JPA.getPath());

        new CodeGenerator(generatorConfig).run();
    }


}
