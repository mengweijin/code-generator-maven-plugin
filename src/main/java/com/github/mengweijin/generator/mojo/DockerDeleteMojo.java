package com.github.mengweijin.generator.mojo;

import lombok.Getter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * 注释说明：@Execute(phase = LifecyclePhase.PACKAGE) 在执行当前 Mojo 前先打包
 * @author mengweijin
 */
@Getter
@Mojo(name = "Docker-Delete")
public class DockerDeleteMojo extends DockerfileGeneratorMojo {

    public static final String DOCKER_IMAGE_DELETE = "DockerImageDelete.bat";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            this.execBash(DOCKER_IMAGE_DELETE);
        } catch (Exception e) {
            getLog().error(e);
            throw new RuntimeException(e);
        }
    }

}
