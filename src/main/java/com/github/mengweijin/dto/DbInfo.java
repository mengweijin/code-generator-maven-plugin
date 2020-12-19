package com.github.mengweijin.dto;

import lombok.Data;

@Data
public class DbInfo {

    private String url;

    private String driverName;

    private String username;

    private String password;
}
