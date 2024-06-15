package com.github.mengweijin.code.generator.mojo;

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * 说明：@Execute(phase = LifecyclePhase.COMPILE) 在运行该目标前，让maven先运行一个并行的生命周期，到指定的阶段为止。到phase执行完，才执行插件目标
 * ResolutionScope.COMPILE_PLUS_RUNTIME:
 * Will add the classpath jars into list when call method project.getRuntimeClasspathElements();
 * maven scope=compile + system + provided + runtime dependencies
 *
 * @author mengweijin
 */
@Execute(phase = LifecyclePhase.COMPILE)
@Mojo(
        name = "code",
        aggregator = true,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class CodeMojo extends AbstractFileSystemGeneratorMojo {

}
