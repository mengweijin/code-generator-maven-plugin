package com.github.mengweijin.generator.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author mengweijin
 */
@Mojo(name = "customer", threadSafe = true)
public class CustomerGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
//        ConfigProperty configProperty = getConfigProperty();
//        new CodeGenerator(configProperty).run();
    }
}
