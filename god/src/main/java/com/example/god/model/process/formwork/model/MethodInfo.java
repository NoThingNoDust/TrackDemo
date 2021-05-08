package com.example.god.model.process.formwork.model;

import com.example.god.model.trace.util.MethodType;

/**
 * 执行的方法信息
 */
public class MethodInfo extends ExecuteInfo {
    /**
     * 类名
     */
    protected String className;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 方法类型
     */
    protected MethodType methodType;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }
}
