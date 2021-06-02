package com.github.mengweijin.generator.entity;

import cn.hutool.system.SystemUtil;
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
    private String outputPackage = "com.github.mengweijin";

    private String author = SystemUtil.get("user.name", false);

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

    /**
     * For example: SYS_, FTL_
     * Note: Separated by commas.
     */
    private String[] tablePrefix;

    private String superEntityClass;
    private String superDaoClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;

    /**
     * 【实体】是否为lombok模型（默认 true）
     */
    private boolean lombokModel = true;
}
