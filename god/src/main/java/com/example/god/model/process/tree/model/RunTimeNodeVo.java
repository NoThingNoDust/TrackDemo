package com.example.god.model.process.tree.model;

/**
 * 树形执行方法执行记录
 */
public class RunTimeNodeVo extends RunTimeNode {
    private String name;
    private Double value = 0.0;


    @Override
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}