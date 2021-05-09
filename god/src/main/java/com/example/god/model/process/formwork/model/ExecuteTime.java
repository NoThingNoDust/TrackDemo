package com.example.god.model.process.formwork.model;

/**
 * 事务执行信息
 */
public class ExecuteTime {
    /**
     * 平均执行时间
     */
    protected Double avgRunTime = 0.0;
    /**
     * 平均执行时间单位
     */
    protected String avgRunTimeUnit = "ms";

    public Double getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Double avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public String getAvgRunTimeUnit() {
        return avgRunTimeUnit;
    }

    public void setAvgRunTimeUnit(String avgRunTimeUnit) {
        this.avgRunTimeUnit = avgRunTimeUnit;
    }
}