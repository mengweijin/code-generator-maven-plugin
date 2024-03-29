package com.github.mengweijin.generator.mojo;

import lombok.Getter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 注释说明：@Execute(phase = LifecyclePhase.PACKAGE) 在执行当前 Mojo 前先打包
 * @author mengweijin
 */
@Getter
@Mojo(name = "Dockerfile")
@Execute(phase = LifecyclePhase.PACKAGE)
public class DockerfileGeneratorMojo extends AbstractDockerMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            this.generate();
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

}
