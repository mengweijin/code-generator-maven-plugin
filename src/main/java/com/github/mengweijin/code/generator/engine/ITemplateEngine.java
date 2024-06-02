package com.github.mengweijin.code.generator.engine;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.mengweijin.code.generator.dto.Config;
import com.github.mengweijin.code.generator.util.GeneratorUtils;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
public interface ITemplateEngine {

    ITemplateEngine init(Config config, List<TableInfo> tableInfoList);

    void write();

    default Map<String, Object> getObjectMap(Config config, TableInfo tableInfo) {
        List<String> baseEntityColumns = GeneratorUtils.resolveBaseEntityColumns(config);
        String entityName = GeneratorUtils.resolveEntityName(tableInfo.getName(), config);
        List<TableField> entityFields = GeneratorUtils.resolveEntityFields(tableInfo, baseEntityColumns);
        List<TableField> commonFields = GeneratorUtils.resolveCommonFields(tableInfo, baseEntityColumns);

        List<String> entityColumns = GeneratorUtils.resolveEntityColumns(entityFields);
        List<String> commonColumns = GeneratorUtils.resolveCommonColumns(commonFields);

        String requestMapping = "/" + StrUtil.toSymbolCase(entityName, '-');
        if(StrUtil.isNotBlank(config.getModuleName())) {
            requestMapping = StrUtil.addPrefixIfNot(config.getModuleName(), "/") + requestMapping;
        }

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("package", config.getPackages());
        objectMap.put("author", config.getAuthor());
        objectMap.put("date", DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATE_PATTERN));
        objectMap.put("baseEntity", config.getBaseEntity());
        objectMap.put("baseEntityPackage", StrUtil.subBefore(config.getBaseEntity(), ".", true));
        objectMap.put("baseEntityName", StrUtil.subAfter(config.getBaseEntity(), ".", true));
        objectMap.put("baseEntityColumns", baseEntityColumns);
        objectMap.put("table", tableInfo);
        objectMap.put("entityName", entityName);
        objectMap.put("entityPropertyName", StrUtil.lowerFirst(entityName));
        objectMap.put("entityFields", entityFields);
        objectMap.put("commonFields", commonFields);
        objectMap.put("allFields", CollUtil.addAll(new ArrayList<>(entityFields), new ArrayList<>(commonFields)));
        objectMap.put("entityColumns", entityColumns);
        objectMap.put("commonColumns", commonColumns);
        objectMap.put("allColumns", CollUtil.addAll(new ArrayList<>(entityColumns), new ArrayList<>(commonColumns)));
        objectMap.put("requestMapping", requestMapping);
        return objectMap;
    }


}
