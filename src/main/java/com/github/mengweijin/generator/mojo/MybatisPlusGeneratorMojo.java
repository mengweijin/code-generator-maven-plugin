package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.enums.Template;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "mybatis-plus", requiresDependencyResolution = ResolutionScope.COMPILE)
public class MybatisPlusGeneratorMojo extends AbstractGeneratorMojo {

    private static final String BaseMapper = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    private static final String IService = "com.baomidou.mybatisplus.extension.service.IService";
    private static final String ServiceImpl = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            CodeGenerator codeGenerator = this.getCodeGenerator();

            String templateLocation = CodeGenerator.TMP_DIR + Template.MYBATIS_PLUS.getPath();
            codeGenerator.getParameters().setTemplateLocation(templateLocation);
            codeGenerator.getParameters().setSuperDaoClass(BaseMapper);
            codeGenerator.getParameters().setSuperServiceClass(IService);
            codeGenerator.getParameters().setSuperServiceImplClass(ServiceImpl);
            codeGenerator.run();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

}
