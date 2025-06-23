package com.tao.db.code;

public enum DataSourceType {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    ;
    private String value;
    DataSourceType(String s) {
        value = s;
    }
    public String getValue() {
        return value;
    }
}
