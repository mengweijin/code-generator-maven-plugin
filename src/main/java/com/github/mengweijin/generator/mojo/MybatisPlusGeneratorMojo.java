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
@Mojo(name = "mybatis-plus", requiresDependencyResolution = ResolutionScope.COMPILE)
public class MybatisPlusGeneratorMojo extends AbstractGeneratorMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            DefaultConfigParameter parameter = this.getGeneratorConfig();
            parameter.initDefaultValue();
            parameter.setTemplateLocation(Template.MYBATIS_PLUS.getPath());
            parameter.setSuperDaoClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
            parameter.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
            parameter.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");

            System.out.println("DefaultConfigParameter: " + parameter);

            new CodeGenerator(parameter).run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }
}
