package com.github.mengweijin.generator.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author mengweijin
 */
@Mojo(name = "mybatis", threadSafe = true)
public class MybatisGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
//        ConfigProperty configProperty = getConfigProperty();
//        configProperty.setTemplate(Template.MYBATIS.getPath());
//
//        new CodeGenerator(configProperty).run();
    }
}
