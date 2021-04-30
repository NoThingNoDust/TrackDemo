package com.example.god.model.jvm;

import lombok.Data;

@Data
public class JvmInfo {
    private String name;
    private Object value;
    private String desc;

    public JvmInfo(String name, Object value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }

    public JvmInfo(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
