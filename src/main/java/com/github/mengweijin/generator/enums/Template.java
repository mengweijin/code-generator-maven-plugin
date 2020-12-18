package com.github.mengweijin.generator.enums;

/**
 * @author mengweijin
 */

public enum Template {

    JPA("templates/jpa/"),
    MYBATIS("templates/mybatis/"),
    MYBATIS_PLUS("templates/mybatis-plus/");

    private final String path;

    Template(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
