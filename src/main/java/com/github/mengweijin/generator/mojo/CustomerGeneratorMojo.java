package com.github.mengweijin.generator.mojo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.mengweijin.generator.entity.Parameters;
import com.github.mengweijin.generator.enums.TemplateType;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import java.io.File;

/**
 * ResolutionScope.COMPILE_PLUS_RUNTIME:
 * Will add the classpath jars into list when call method project.getRuntimeClasspathElements();
 * maven scope=compile + system + provided + runtime dependencies
 *
 * @author mengweijin
 */
@Mojo(name = "Customer", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class CustomerGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void setDefaultFixedParameters(Parameters parameters) {
        getLog().info("Note: The [templateLocation] and correct [templateType] must be configured! The default [templateType] value is btl.");
        String templateLocation = parameters.getTemplateLocation();
        if(!FileUtil.isDirectory(templateLocation)) {
            String msg = "Can't found valid template location, please check your configuration for args templateLocation. ===>";
            getLog().error(msg + templateLocation);
            throw new RuntimeException(msg + templateLocation);
        }

        TemplateType templateType = parameters.getTemplateType();
        File[] tplFiles = FileUtil.file(templateLocation).listFiles((dir, name) -> name.toLowerCase().endsWith(templateType.getSuffix()));
        if(ArrayUtil.isEmpty(tplFiles)) {
            String msg = "Can't found valid template files. Please check your configuration for args [templateLocation] and [templateType]!";
            getLog().error(msg);
            getLog().error("Current args [templateLocation] is: " + templateLocation);
            getLog().error("Current args [templateType] is: " + templateType.name());
            throw new RuntimeException(msg);
        }
    }
}
