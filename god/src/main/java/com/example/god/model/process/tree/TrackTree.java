package com.example.god.model.process.tree;

import lombok.Data;

import java.util.ArrayList;
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
        root = new RunTimeNode();
        root.setChildren(new ArrayList<>());
        now = root;
    }

    /**
     * 新增子节点
     */
    public void addNewNode() {
        RunTimeNode newNode = new RunTimeNode();
        newNode.setParent(now);
        newNode.setChildren(new ArrayList<>());
        now.getChildren().add(newNode);
        now = newNode;
    }

    /**
     * 返回父节点
     */
    public void rollback() {
        if (now != root) {
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
