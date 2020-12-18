package com.github.mengweijin.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.github.mengweijin.generator.enums.TemplateType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author mengweijin
 */
@Slf4j
@Data
public class ConfigProperty {

    /**
     * 可选项：类注释中的@author的值
     */
    private String author = SystemUtil.getUserInfo().getName();

    /**
     * 必填项：用来生成文件的模板的位置(resources目录下):
     * 默认模板：
     * 1. templates/jpa/;
     * 2. templates/mybatis/;
     * 3. templates/mybatis-plus/;
     * 4. 或者自定义resources目录下的任何模板目录
     */
    private String template;

    /**
     * 必填项：生成的代码文件的位置，在src/main/java下的包路径，最后一个小数点后面的名字会作为模块名
     */
    private String outputPackage = "target/code-generator/";

    /**
     * 必填项：要生成代码的数据库表名称（不区分大小写）
     * <tables>
     * <param>table1</param>
     * <param>table2</param>
     * </tables>
     */
    private String[] tables;

    /**
     * 可选项：表前缀
     */
    private String tablePrefix;

    /**
     * 以下五个字段: 可选项
     * 生成的类继承的父类
     */
    private String superEntityClass;
    private String superDaoClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;

    /**
     * 可选项： 与superEntityClass同时使用，父类中的公共字段数据库列名（不区分大小写）
     */
    private String[] superEntityColumns;

    /**
     * 以下四个字段：必填项
     * data source config
     */
    private String dbUrl;

    private String dbDriverName;

    private String dbUsername;

    private String dbPassword;

    /**
     * btl, ftl, vm
     */
    private TemplateType templateType = TemplateType.beetl;

    public void init() {
        if (this.template != null) {
            this.template = this.template.endsWith(StrUtil.SLASH) ? this.template : this.template + StrUtil.SLASH;
        }


    }

    private String[] trimAndDistinct(String[] array) {
        return Arrays.stream(array).map(String::trim).distinct().toArray(String[]::new);
    }

}
