package com.github.mengweijin.generator;

import lombok.Data;

/**
 * @author mengweijin
 */
@Data
public class DbInfo {

    private String url;

    private String driverName;

    private String username;

    private String password;
}
