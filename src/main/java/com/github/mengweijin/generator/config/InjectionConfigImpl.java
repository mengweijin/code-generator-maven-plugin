package com.github.mengweijin.generator.config;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.mengweijin.generator.dto.ConfigParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author mengweijin
 */
public class InjectionConfigImpl extends InjectionConfig {

    public static final String OUTPUT_PATH_PREFIX_REGEX = "^src/((main)|(test))/java/";

    private final ConfigParameter configParameter;

    private final AutoGenerator autoGenerator;

    public InjectionConfigImpl(ConfigParameter configParameter, AutoGenerator autoGenerator) {
        this.configParameter = configParameter;
        this.autoGenerator = autoGenerator;
    }

    @Override
    public void initMap() {

    }

    @Override
    public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("author", objectMap.get("author"));
        map.put("date", objectMap.get("date"));
        map.put("superEntityClassPackage", configParameter.getSuperEntityClass());
        map.put("superDaoClassPackage", configParameter.getSuperDaoClass());
        map.put("superServiceClassPackage", configParameter.getSuperServiceClass());
        map.put("superServiceImplClassPackage", configParameter.getSuperServiceImplClass());
        map.put("superControllerClassPackage", configParameter.getSuperControllerClass());

        map.put("superEntityName", StrUtil.subAfter(configParameter.getSuperEntityClass(), StrUtil.DOT, true));
        map.put("superDaoName", StrUtil.subAfter(configParameter.getSuperDaoClass(), StrUtil.DOT, true));
        map.put("superServiceName", StrUtil.subAfter(configParameter.getSuperServiceClass(), StrUtil.DOT, true));
        map.put("superServiceImplName", StrUtil.subAfter(configParameter.getSuperServiceImplClass(), StrUtil.DOT, true));
        map.put("superControllerName", StrUtil.subAfter(configParameter.getSuperControllerClass(), StrUtil.DOT, true));

        map.put("entityName", objectMap.get("entity"));
        map.put("entityVariableName", StrUtil.lowerFirst(String.valueOf(objectMap.get("entity"))));

        String basePackage;
        if(ReUtil.isMatch(OUTPUT_PATH_PREFIX_REGEX, configParameter.getOutputPath())) {
            basePackage = StrUtil.subAfter(configParameter.getOutputPath(), "java/", false);
        } else {
            basePackage = "";
        }
        map.put("basePackage", basePackage);
        map.put("moduleName", autoGenerator.getPackageInfo().getModuleName());

        map.put("table", objectMap.get("table"));

        map.put("fieldImportPackages", handleFieldImportPackage((TableInfo) objectMap.get("table")));
        map.put("idPropertyType", handleIdPropertyType((TableInfo) objectMap.get("table")));
        map.put("idField", handleIdField((TableInfo) objectMap.get("table")));
        map.put("allFieldList", handleAllFieldList((TableInfo) objectMap.get("table")));

        return map;
    }

    private Set<String> handleFieldImportPackage(TableInfo tableInfo) {
        Set<String> set = new HashSet<>();
        tableInfo.getFields().forEach(tableField -> {
            IColumnType columnType = tableField.getColumnType();
            if (columnType.getPkg() != null) {
                set.add(columnType.getPkg());
            }
        });

        return set;
    }

    private String handleIdPropertyType(TableInfo tableInfo) {
        Optional<String> optional = tableInfo.getFields().stream()
                .filter(TableField::isKeyFlag)
                .map(tableField -> tableField.getColumnType().getType()).findFirst();
        return optional.orElse(DbColumnType.LONG.getType());
    }

    private TableField handleIdField(TableInfo tableInfo) {
        Optional<TableField> optional = tableInfo.getFields().stream().filter(TableField::isKeyFlag).findFirst();
        return optional.orElse(new TableField());
    }

    private List<TableField> handleAllFieldList(TableInfo tableInfo) {
        List<TableField> fields = tableInfo.getFields();
        List<TableField> commonFields = tableInfo.getCommonFields();
        List<TableField> allFields = new ArrayList<>();
        allFields.addAll(fields);
        allFields.addAll(commonFields);
        return allFields;
    }
}
