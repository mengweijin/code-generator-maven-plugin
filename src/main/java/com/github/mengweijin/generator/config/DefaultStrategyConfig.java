package com.github.mengweijin.generator.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.github.mengweijin.generator.CodeGenerator;
import com.github.mengweijin.generator.Parameters;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author mengweijin
 */
public class DefaultStrategyConfig extends StrategyConfig {

    private CodeGenerator codeGenerator;

    public DefaultStrategyConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
        this.init();
    }

    /**
     * Initialize the default parameter.
     */
    public void init() {
        Parameters parameters = codeGenerator.getParameters();

        this.setNaming(NamingStrategy.underline_to_camel);
        this.setColumnNaming(NamingStrategy.underline_to_camel);
        this.setEntityLombokModel(parameters.isLombokModel());
        this.setRestControllerStyle(true);
        this.setControllerMappingHyphenStyle(true);
        this.setEntityTableFieldAnnotationEnable(true);
        this.setInclude(trimTableName(parameters.getTables()));
        this.setTablePrefix(parameters.getTablePrefix());


        this.setSuperEntityClass(parameters.getSuperEntityClass());

        if (this.getSuperEntityClass() != null && this.getSuperEntityColumns() == null) {
            this.setSuperEntityColumns(this.generateDefaultSuperEntityColumns());
        } else {
            this.setSuperEntityColumns(parameters.getSuperEntityColumns());
        }

        this.setSuperMapperClass(parameters.getSuperDaoClass());
        this.setSuperServiceClass(parameters.getSuperServiceClass());
        this.setSuperServiceImplClass(parameters.getSuperServiceImplClass());
        this.setSuperControllerClass(parameters.getSuperControllerClass());
    }

    /**
     * If the user configured superEntityColumns, the configuration will prevail;
     * if not, the default configuration of superEntityColumns will be generated according to the superEntityClass.
     *
     * @return String[]
     */
    private String[] generateDefaultSuperEntityColumns() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName(this.getSuperEntityClass(), true, classLoader);
            Field[] declaredFields = ClassUtil.getDeclaredFields(cls);
            return Arrays.stream(declaredFields)
                    .map(field -> StrUtil.toUnderlineCase(field.getName())).toArray(String[]::new);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String[] trimTableName(String[] tables) {
        if(tables == null) {
            return null;
        }
        return Arrays.stream(tables).map(String::trim).toArray(String[]::new);
    }
}
