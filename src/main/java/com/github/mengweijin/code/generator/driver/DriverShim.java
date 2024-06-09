package com.github.mengweijin.code.generator.driver;

import lombok.Getter;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 使用了 DriverShim 包装类后，在 getConnection() 时 DriverManager 同样要验证驱动是否对应用程序类加载器可见，
 * 只是这时候要验证的是这个 DriverShim 而非通过自定义类加载器弄进来的 “com.mysql.jdbc.Driver” 了。
 * 而后在使用 DriverShim 桥接到实际的 com.mysql.jdbc.Driver 时 DriverManager 就管不着了。
 * 大致意思就是：你 DriverManager 不是想验证数据库驱动是否是应用程序类加载器加载的吗？给个壳逗你玩一下，
 * 我在壳里面呢，你管我是由哪个类加载器加载的。
 * @author mengweijin
 * @date 2022/10/30
 */
@Getter
public class DriverShim implements Driver {

    private Driver driver;

    DriverShim(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return this.driver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.driver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
