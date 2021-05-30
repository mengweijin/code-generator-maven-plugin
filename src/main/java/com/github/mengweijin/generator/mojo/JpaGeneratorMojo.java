package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.entity.ProjectInfo;
import com.github.mengweijin.generator.enums.Template;
import com.github.mengweijin.generator.enums.TemplateType;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Execute(phase = LifecyclePhase.COMPILE)
@Mojo(name = "jpa", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class JpaGeneratorMojo extends AbstractGeneratorMojo {

    private static final String JpaRepository = "org.springframework.data.jpa.repository.JpaRepository";

    @Override
    protected void setDefaultFixedParameters(Parameters parameters) {
        parameters.setTemplateLocation(ProjectInfo.TMP_DIR + Template.JPA.getPath());
        parameters.setSuperDaoClass(JpaRepository);
        parameters.setTemplateType(TemplateType.beetl);
    }
}
