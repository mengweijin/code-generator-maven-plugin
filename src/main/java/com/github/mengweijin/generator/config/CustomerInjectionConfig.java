package com.github.mengweijin.generator.config;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.mengweijin.generator.entity.IdField;
import com.github.mengweijin.generator.entity.Parameters;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mengweijin
 */
@Slf4j
public class CustomerInjectionConfig extends InjectionConfig {

    private Parameters parameters;

    public CustomerInjectionConfig(Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void prepareObjectMap(Map<String, Object> objectMap) {
        objectMap.remove("package");
        objectMap.put("parameters", parameters);
        objectMap.put("idField", getIdField((TableInfo) objectMap.get("table")));
        objectMap.put("allFieldList", handleAllFieldList((TableInfo) objectMap.get("table")));

        log.info("Beetl parameter map: {}", objectMap);
    }

    private IdField getIdField(TableInfo tableInfo) {
        TableField tableField = tableInfo.getFields().stream().filter(TableField::isKeyFlag).findFirst().orElse(null);

        IdField idField = new IdField();
        if(tableField != null) {
            idField.setColumnName(tableField.getName());
            idField.setPropertyName(tableField.getPropertyName());
            idField.setPropertyType(tableField.getColumnType().getType());
        }
        return idField;
    }

    private List<TableField> handleAllFieldList(TableInfo tableInfo) {
        List<TableField> fieldList = tableInfo.getFields();
        List<TableField> commonFields = tableInfo.getCommonFields();

        // 为了不影响其他地方的引用，这里必须创建一个新的集合来存放所有字段
        List<TableField> allList = new ArrayList<>();
        allList.addAll(fieldList);
        allList.addAll(commonFields);
        return allList;
    }
}
