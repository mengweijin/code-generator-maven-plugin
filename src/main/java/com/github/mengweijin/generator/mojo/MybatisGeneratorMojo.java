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
@Mojo(name = "mybatis", requiresDependencyResolution = ResolutionScope.COMPILE)
public class MybatisGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        DefaultConfigParameter defaultConfigParameter = this.getGeneratorConfig();
        defaultConfigParameter.initDefaultValue();
        defaultConfigParameter.setTemplateLocation(Template.MYBATIS.getPath());

        new CodeGenerator(defaultConfigParameter).run();
    }
}
