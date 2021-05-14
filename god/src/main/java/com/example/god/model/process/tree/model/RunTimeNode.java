package com.example.god.model.process.tree.model;

import com.example.god.model.process.formwork.model.ExecuteTime;
import com.example.god.model.process.formwork.model.MethodInfo;

import java.util.Objects;

/**
 * 树形方法执行记录
 */
public class RunTimeNode extends TreeNode<RunTimeNode> implements Comparable<RunTimeNode> {


    private String name;

    private Double value = 0.0;

    /**
     * 方法信息
     */
    private MethodInfo methodInfo;
    /**
     * 执行信息
     */
    private ExecuteTime executeTime;


    public MethodInfo getMethodInfo() {
        if (Objects.isNull(methodInfo)) {
            methodInfo = new MethodInfo();
        }
        return methodInfo;
    }

    public void setMethodInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    public ExecuteTime getExecuteTime() {
        if (Objects.isNull(executeTime)) {
            executeTime = new ExecuteTime();
        }
        return executeTime;
    }

    public void setExecuteTime(ExecuteTime executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public int compareTo(RunTimeNode ob) {
        return this.executeTime.getAvgRunTime().compareTo(ob.getExecuteTime().getAvgRunTime());
    }

    public void print() {
//        System.out.println(methodInfo.getClassName() + "." + methodInfo.getMethodName() + "调用了" + children.size() + "个方法======耗时" + this.executeTime.getAvgRunTime());
//        for (RunTimeNode child : children) {
//            child.print();
//        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return executeTime.getAvgRunTime();
    }
}