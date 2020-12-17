package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.code.CodeGenerator;
import com.github.mengweijin.generator.code.ConfigProperty;
import com.github.mengweijin.generator.code.ETemplate;
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
        ConfigProperty configProperty = getConfigProperty();
        configProperty.setTemplate(ETemplate.JPA.getPath());


        new CodeGenerator(configProperty).run();
    }
}
