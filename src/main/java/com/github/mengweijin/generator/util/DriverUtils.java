package com.github.mengweijin.generator.util;

import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.dialect.DialectFactory;

/**
 * Change from cn.hutool.db.dialect.DialectFactory
 *
 * @author mengweijin
 */
public class DriverUtils {

    /**
     * 修改点：
     * nameContainsProductInfo = nameContainsProductInfo.split(";")[0];
     * ClassLoaderUtil.isPresent(DialectFactory.DRIVER_MYSQL_V6, classLoader)
     * ClassLoaderUtil.isPresent(DialectFactory.DRIVER_ORACLE, classLoader)
     *
     * @param nameContainsProductInfo
     * @return
     */
    public static String identifyDriver(String nameContainsProductInfo) {
        if (StrUtil.isBlank(nameContainsProductInfo)) {
            return null;
        }
        // 全部转为小写，忽略大小写
        nameContainsProductInfo = StrUtil.cleanBlank(nameContainsProductInfo.toLowerCase());

        nameContainsProductInfo = nameContainsProductInfo.split(";")[0];

        String driver = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (nameContainsProductInfo.contains("mysql")) {
            driver = ClassLoaderUtil.isPresent(DialectFactory.DRIVER_MYSQL_V6, classLoader) ? DialectFactory.DRIVER_MYSQL_V6 : DialectFactory.DRIVER_MYSQL;
        } else if (nameContainsProductInfo.contains("oracle")) {
            driver = ClassLoaderUtil.isPresent(DialectFactory.DRIVER_ORACLE, classLoader) ? DialectFactory.DRIVER_ORACLE : DialectFactory.DRIVER_ORACLE_OLD;
        } else if (nameContainsProductInfo.contains("postgresql")) {
            driver = DialectFactory.DRIVER_POSTGRESQL;
        } else if (nameContainsProductInfo.contains("sqlite")) {
            driver = DialectFactory.DRIVER_SQLLITE3;
        } else if (nameContainsProductInfo.contains("sqlserver")) {
            driver = DialectFactory.DRIVER_SQLSERVER;
        } else if (nameContainsProductInfo.contains("hive")) {
            driver = DialectFactory.DRIVER_HIVE;
        } else if (nameContainsProductInfo.contains("h2")) {
            driver = DialectFactory.DRIVER_H2;
        } else if (nameContainsProductInfo.contains("derby")) {
            // 嵌入式Derby数据库
            driver = DialectFactory.DRIVER_DERBY;
        } else if (nameContainsProductInfo.contains("hsqldb")) {
            // HSQLDB
            driver = DialectFactory.DRIVER_HSQLDB;
        } else if (nameContainsProductInfo.contains("dm")) {
            // 达梦7
            driver = DialectFactory.DRIVER_DM7;
        } else if (nameContainsProductInfo.contains("kingbase8")) {
            // 人大金仓8
            driver = DialectFactory.DRIVER_KINGBASE8;
        } else if (nameContainsProductInfo.contains("ignite")) {
            // Ignite thin
            driver = DialectFactory.DRIVER_IGNITE_THIN;
        } else if (nameContainsProductInfo.contains("clickhouse")) {
            // ClickHouse
            driver = DialectFactory.DRIVER_CLICK_HOUSE;
        }

        return driver;
    }
}
