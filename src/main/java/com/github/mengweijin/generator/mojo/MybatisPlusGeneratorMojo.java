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
@Mojo(name = "mybatis-plus", threadSafe = true)
public class MybatisPlusGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ConfigProperty configProperty = getConfigProperty();
        configProperty.setTemplate(ETemplate.MYBATIS_PLUS.getPath());

        new CodeGenerator(configProperty).run();
    }
}
