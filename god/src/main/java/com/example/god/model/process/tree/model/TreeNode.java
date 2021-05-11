package com.example.god.model.process.tree.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public abstract class TreeNode<T extends TreeNode<T>> {
//    @JSONField(serialize = false) 关闭关闭序列化，不然前端拿不到数据
    protected List<T> children;
    @JSONField(serialize = false)
    protected T parent;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }
}