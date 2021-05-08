package com.example.god.model.process.tree;

/**
 * 树形执行方法执行记录
 */
public class RunTimeNodeVo extends RunTimeNode {
    private String name;
    private Double value = 0.0;


    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
