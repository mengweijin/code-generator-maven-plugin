package com.github.mengweijin.generator.code;

/**
 * @author mengweijin
 */

public enum ETemplateSuffix {

    beetl("btl"),
    velocity("vm"),
    freemarker("ftl");

    private final String suffix;

    ETemplateSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
