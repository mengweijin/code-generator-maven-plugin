package com.github.mengweijin.generator;

import com.github.mengweijin.generator.enums.TemplateType;
import lombok.Data;

/**
 * @author mengweijin
 */
@Data
public class Parameters {

    /**
     * Absolute path
     */
    private String outputDir;

    private String author;

    private String templateLocation;

    private TemplateType templateType = TemplateType.beetl;

    private DbInfo dbInfo;

    private String[] tables;

    private String[] tablePrefix;

    private String superEntityClass;
    private String superDaoClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private String[] superEntityColumns;
}
