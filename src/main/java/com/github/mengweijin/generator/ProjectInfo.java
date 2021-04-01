package com.github.mengweijin.generator;

import cn.hutool.system.SystemUtil;
import lombok.Data;
import org.apache.maven.model.Resource;

import java.io.File;
import java.util.List;

/**
 * @author mengweijin
 */
@Data
public class ProjectInfo {

    public static final String TMP_DIR = SystemUtil.get(SystemUtil.TMPDIR) + "code-generator/";

    private Parameters parameters;

    private List<Resource> resourceList;

    private File baseDir;

    private File sourceDir;


}
