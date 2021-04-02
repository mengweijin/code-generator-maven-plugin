package com.github.mengweijin.generator.mojo;

import com.github.mengweijin.generator.entity.Parameters;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * @author mengweijin
 */
@Mojo(name = "customer", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class CustomerGeneratorMojo extends AbstractGeneratorMojo {

    @Override
    protected void setDefaultFixedParameters(Parameters parameters) {

    }
}
