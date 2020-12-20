package com.github.mengweijin.generator.dto;

import com.github.mengweijin.generator.enums.TemplateType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
     * default: target/code-generator/
     * sample: src/main/java/com/mengweijin/aaaaaaaaaaa
     */
    private String outputPath;

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
