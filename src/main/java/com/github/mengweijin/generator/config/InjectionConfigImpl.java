package com.github.mengweijin.generator.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.mengweijin.generator.code.ConfigProperty;

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

    private final ConfigProperty configProperty;

    private final AutoGenerator autoGenerator;

    public InjectionConfigImpl(ConfigProperty configProperty, AutoGenerator autoGenerator) {
        this.configProperty = configProperty;
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
        map.put("superEntityClassPackage", configProperty.getSuperEntityClass());
        map.put("superDaoClassPackage", configProperty.getSuperDaoClass());
        map.put("superServiceClassPackage", configProperty.getSuperServiceClass());
        map.put("superServiceImplClassPackage", configProperty.getSuperServiceImplClass());
        map.put("superControllerClassPackage", configProperty.getSuperControllerClass());

        map.put("superEntityName", StrUtil.subAfter(configProperty.getSuperEntityClass(), StrUtil.DOT, true));
        map.put("superDaoName", StrUtil.subAfter(configProperty.getSuperDaoClass(), StrUtil.DOT, true));
        map.put("superServiceName", StrUtil.subAfter(configProperty.getSuperServiceClass(), StrUtil.DOT, true));
        map.put("superServiceImplName", StrUtil.subAfter(configProperty.getSuperServiceImplClass(), StrUtil.DOT, true));
        map.put("superControllerName", StrUtil.subAfter(configProperty.getSuperControllerClass(), StrUtil.DOT, true));

        map.put("entityName", objectMap.get("entity"));
        map.put("entityVariableName", StrUtil.lowerFirst(String.valueOf(objectMap.get("entity"))));

        map.put("basePackage", configProperty.getOutputPackage());
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
