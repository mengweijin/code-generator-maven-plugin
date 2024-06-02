package com.github.mengweijin.code.generator.enums;

/**
 * @author mengweijin
 */

public enum TemplateType {

    velocity("vm"), freemarker("ftl"), beetl("btl"), enjoy("ej");

    private final String suffix;

    TemplateType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
