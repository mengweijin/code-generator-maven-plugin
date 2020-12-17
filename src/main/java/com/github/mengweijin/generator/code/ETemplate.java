package com.github.mengweijin.generator.code;

/**
 * @author mengweijin
 */

public enum ETemplate {

    JPA("templates/jpa/"),
    MYBATIS("templates/mybatis/"),
    MYBATIS_PLUS("templates/mybatis-plus/");

    private final String path;

    ETemplate(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
