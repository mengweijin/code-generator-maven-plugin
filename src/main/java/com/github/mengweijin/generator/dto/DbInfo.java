package com.github.mengweijin.generator.dto;

import lombok.Data;

@Data
public class DbInfo {

    private String url;

    private String driverName;

    private String username;

    private String password;
}
