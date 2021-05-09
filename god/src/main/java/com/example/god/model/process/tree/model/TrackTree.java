package com.example.god.model.process.tree.model;

import com.example.god.model.process.formwork.model.ExecuteTime;
import com.example.god.model.process.formwork.model.MethodInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 树形保存方法调用记录
 */
@Data
public class TrackTree {
    /**
     * 根节点
     */
    private RunTimeNode root;
    /**
     * 当前节点
     */
    private RunTimeNode now;

    public TrackTree() {
        //伪根
        root = new RunTimeNode();
        root.setChildren(new ArrayList<>());
        now = root;
    }

    /**
     * 新增子节点
     */
    public void addNewNode(MethodInfo methodInfo) {
        RunTimeNode newNode = new RunTimeNode();
        newNode.setParent(now);
        newNode.setChildren(new ArrayList<>(Collections.singletonList(now)));
        newNode.setMethodInfo(methodInfo);
        now = newNode;
    }

    /**
     * 返回父节点
     */
    public void rollback(ExecuteTime executeTime) {
        if (now != root) {
            now.setExecuteTime(executeTime);
            now = now.getParent();
        }
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public RunTimeNode getRoot() {
        List<RunTimeNode> children = root.getChildren();
        if (children.isEmpty()) {
            return null;
        }
        return children.get(0);
    }
}
