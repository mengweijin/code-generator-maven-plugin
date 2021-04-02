package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.enums.Template;
import com.github.mengweijin.generator.enums.TemplateType;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "mybatis-plus", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class MybatisPlusGeneratorMojo extends AbstractGeneratorMojo {

    private static final String BaseMapper = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
    private static final String IService = "com.baomidou.mybatisplus.extension.service.IService";
    private static final String ServiceImpl = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";

    @Override
    protected void setDefaultFixedParameters(Parameters parameters) {
        parameters.setTemplateLocation(ProjectInfo.TMP_DIR + Template.MYBATIS_PLUS.getPath());
        parameters.setSuperDaoClass(BaseMapper);
        parameters.setSuperServiceClass(IService);
        parameters.setSuperServiceImplClass(ServiceImpl);
        parameters.setTemplateType(TemplateType.beetl);
    }
}
