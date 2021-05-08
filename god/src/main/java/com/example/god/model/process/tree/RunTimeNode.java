package com.example.god.model.process.tree;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.god.model.process.formwork.model.MethodInfo;

import java.util.List;

/**
 * 树形执行方法执行记录
 */
public class RunTimeNode extends MethodInfo implements Comparable<RunTimeNode> {

    @JSONField(serialize = false)
    private List<RunTimeNode> children;
    @JSONField(serialize = false)
    private RunTimeNode parent;

    public List<RunTimeNode> getChildren() {
        return children;
    }

    public void setChildren(List<RunTimeNode> children) {
        this.children = children;
    }

    public RunTimeNode getParent() {
        return parent;
    }

    public void setParent(RunTimeNode parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(RunTimeNode ob) {
        return this.avgRunTime.compareTo(ob.getAvgRunTime());
    }

    public void print() {
        System.out.println(className + "." + methodName + "调用了" + children.size() + "个方法======耗时" + avgRunTime);
        for (RunTimeNode child : children) {
            child.print();
        }
    }
}
