package com.github.mengweijin.generator.enums;

/**
 * @author mengweijin
 */

public enum TemplateType {

    beetl("btl"), velocity("vm"), freemarker("ftl");

    private final String suffix;

    TemplateType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
