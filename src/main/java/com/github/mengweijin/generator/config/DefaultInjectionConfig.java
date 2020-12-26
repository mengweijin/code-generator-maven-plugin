package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.github.mengweijin.generator.CodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
public class DefaultInjectionConfig extends InjectionConfig {

    private CodeGenerator codeGenerator;

    public DefaultInjectionConfig(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void initMap() {
    }

    @Override
    public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
        objectMap.remove("package");
        objectMap.put("autoGenerator", codeGenerator.getAutoGenerator());
        objectMap.put("idField", handleIdField((TableInfo) objectMap.get("table")));
        objectMap.put("idPropertyType", handleIdPropertyType((TableInfo) objectMap.get("table")));
        objectMap.put("allFieldList", handleAllFieldList((TableInfo) objectMap.get("table")));
        return objectMap;
    }

    private TableField handleIdField(TableInfo tableInfo) {
        return tableInfo.getFields().stream().filter(TableField::isKeyFlag).findFirst().orElse(new TableField());
    }

    private String handleIdPropertyType(TableInfo tableInfo) {
        return tableInfo.getFields().stream()
                .filter(TableField::isKeyFlag)
                .map(tableField -> tableField.getColumnType().getType())
                .findFirst()
                .orElse(DbColumnType.LONG.getType());
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
