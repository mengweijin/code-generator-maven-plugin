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
 * ResolutionScope.COMPILE_PLUS_RUNTIME:
 * Will add the classpath jars into list when call method project.getRuntimeClasspathElements();
 * maven scope=compile + system + provided + runtime dependencies
 *
 * @author mengweijin
 */
@Execute(phase = LifecyclePhase.COMPILE)
@Mojo(name = "mybatis", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class MybatisGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void setDefaultFixedParameters(Parameters parameters) {
        parameters.setTemplateLocation(ProjectInfo.TMP_DIR + Template.MYBATIS.getPath());
        parameters.setTemplateType(TemplateType.beetl);
    }
}
