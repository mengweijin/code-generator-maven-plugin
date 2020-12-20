package com.github.mengweijin.generator.dto;

import com.github.mengweijin.generator.enums.TemplateType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mengweijin
 */
@Data
@Accessors(chain = true)
public class ConfigParameter {

    private String author;

    private String templateLocation;

    private TemplateType templateType;

    /**
     * default生成在src/test/java下。
     * sample: com.github.mengweijin.aaaaaaaaaaa
     * sample: com/github/mengweijin/aaaaaaaaaaa
     */
    private String outputPackage;

    private String[] tables;

    private String[] tablePrefix;

    private String superEntityClass;
    private String superDaoClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private String[] superEntityColumns;

    private DbInfo dbInfo;
}
