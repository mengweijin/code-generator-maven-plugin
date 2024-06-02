package com.github.mengweijin.code.generator.util;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.github.mengweijin.code.generator.dto.Config;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
@Slf4j
public class GeneratorUtils {

    /**
     * If the user configured superEntityColumns, the configuration will prevail;
     * if not, the default configuration of superEntityColumns will be generated according to the superEntityClass.
     *
     * @return String
     */
    public static List<String> resolveBaseEntityColumns(Config config) {
        String baseEntity = config.getBaseEntity();
        if(StrUtil.isBlank(baseEntity)) {
            return new ArrayList<>();
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> cls = Class.forName(baseEntity, true, classLoader);
            Field[] declaredFields = FieldUtil.getFieldsDirectly(cls, true);
            return Arrays.stream(declaredFields).map(field -> StrUtil.toUnderlineCase(field.getName()).toUpperCase()).collect(Collectors.toList());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String[] trimItems(String[] items) {
        if (items == null) {
            return new String[]{};
        }
        return Arrays.stream(items).map(String::trim).toArray(String[]::new);
    }

    public static String resolveEntityName(String tableName, Config config) {
        String val = tableName;
        if (config.getTablePrefix() != null) {
            for (String tablePrefix : config.getTablePrefix()) {
                if(tableName.toLowerCase().startsWith(tablePrefix.toLowerCase())) {
                    val = tableName.substring(tablePrefix.length());
                    break;
                }
            }
        }
        return StrUtil.upperFirst(StrUtil.toCamelCase(val.toLowerCase()));
    }

    public static List<String> resolveCommonColumns(List<TableField> commonFields) {
        return commonFields.stream().map(tableField -> tableField.getColumnName().toUpperCase()).collect(Collectors.toList());
    }

    public static List<String> resolveEntityColumns(List<TableField> entityFields) {
        return entityFields.stream().map(tableField -> tableField.getColumnName().toUpperCase()).collect(Collectors.toList());
    }

    public static List<TableField> resolveCommonFields(TableInfo tableInfo, List<String> baseEntityColumns) {
        return  tableInfo.getFields().stream().filter(tableField -> baseEntityColumns.contains(tableField.getColumnName().toUpperCase())).collect(Collectors.toList());
    }

    public static List<TableField> resolveEntityFields(TableInfo tableInfo, List<String> baseEntityColumns) {
        return tableInfo.getFields().stream().filter(tableField -> !baseEntityColumns.contains(tableField.getColumnName().toUpperCase())).collect(Collectors.toList());
    }

    public static String renderString(String content, Map<String, Object> map){
        Set<Map.Entry<String, Object>> sets = map.entrySet();
        for(Map.Entry<String, Object> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(StrUtil.toString(entry.getValue()));
        }
        return content;
    }
}
