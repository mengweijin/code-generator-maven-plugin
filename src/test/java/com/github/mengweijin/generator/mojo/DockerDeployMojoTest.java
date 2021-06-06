package com.github.mengweijin.generator.mojo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

public class DockerDeployMojoTest {

    @Test
    public void callCmd() {
        String str = RuntimeUtil.execForStr("cmd /c cd C:\\Program Files (x86)");
        System.out.println(str);
    }

    @Test
    public void diskStore() {
        File file = FileUtil.file("c:\\Program Files (x86)");
        String mount = StrUtil.subPre(file.getAbsolutePath(), 2).toUpperCase(Locale.ROOT);
        Assertions.assertEquals("C:", mount);
    }



}