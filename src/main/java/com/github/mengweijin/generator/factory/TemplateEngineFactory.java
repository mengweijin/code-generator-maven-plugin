package com.github.mengweijin.generator.factory;

import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.github.mengweijin.generator.engine.BeetlStringTemplateEngine;
import com.github.mengweijin.generator.enums.TemplateType;

import java.util.HashMap;

/**
 * @author mengweijin
 */
public class TemplateEngineFactory {

    public static final HashMap<TemplateType, AbstractTemplateEngine> map = new HashMap<>(3);

    static {
        map.put(TemplateType.beetl, new BeetlStringTemplateEngine());
        map.put(TemplateType.freemarker, new FreemarkerTemplateEngine());
        map.put(TemplateType.velocity, new VelocityTemplateEngine());
    }

    private TemplateEngineFactory() {
    }

    public static AbstractTemplateEngine getTemplateEngine(TemplateType templateType) {
        AbstractTemplateEngine engine = map.get(templateType);
        if(engine == null) {
            throw new RuntimeException("TemplateType can't be null!");
        }

        return engine;
    }
}
