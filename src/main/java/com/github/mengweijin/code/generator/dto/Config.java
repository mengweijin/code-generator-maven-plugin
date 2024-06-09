package com.github.mengweijin.code.generator.dto;

import lombok.Data;
import org.dromara.hutool.core.util.SystemUtil;

/**
 * @author mengweijin
 */
@Data
public class Config {

    private String packages;

    private String moduleName;

    private String author = SystemUtil.get("user.name", false);

    private String outputDir = "/target/code-generator";

    private String templateDir;

    /**
     * Optional for Spring Boot if exists application.yml.
     */
    private DbInfo dbInfo;

    /**
     * Optional.
     * For example: SYS_, VTL_
     * Note: Separated by commas.
     */
    private String[] tablePrefix;

    /**
     * Optional.
     */
    private String baseEntity;

}
