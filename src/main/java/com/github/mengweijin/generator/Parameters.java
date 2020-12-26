package com.github.mengweijin.generator;

import com.github.mengweijin.generator.enums.TemplateType;
import lombok.Data;

/**
 * @author mengweijin
 */
@Data
public class Parameters {

    /**
     * default: com.github.mengweijin
     */
    private String outputPackage;

    private String author;

    /**
     * The absolute path to the folder.
     */
    private String templateLocation;

    private TemplateType templateType = TemplateType.beetl;

    private DbInfo dbInfo;

    /**
     * For example: SYS_USER, SYS_ROLE
     * Note: Separated by commas.
     */
    private String[] tables;

    private String[] tablePrefix;

    private String superEntityClass;
    private String superDaoClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private String[] superEntityColumns;
}
