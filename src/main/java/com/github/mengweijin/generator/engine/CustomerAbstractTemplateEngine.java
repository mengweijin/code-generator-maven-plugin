package com.github.mengweijin.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;

import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
public abstract class CustomerAbstractTemplateEngine extends AbstractTemplateEngine {

    @Override
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        String output;
                        for (FileOutConfig foc : focList) {
                            output = foc.outputFile(tableInfo);
                            if (isCreate(FileType.OTHER, output)) {
                                writer(objectMap, foc.getTemplatePath(), output);
                            } else {
                                System.out.println("Warning: File already exists when generated! " + output);
                            }
                        }
                    } else {

                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception: Can't create file, please check the configuration! " + e.toString());
            logger.error("Can't create file, please check the configuration!", e);
        }
        return this;
    }
}
