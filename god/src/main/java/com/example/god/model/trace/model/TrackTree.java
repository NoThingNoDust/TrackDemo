package com.example.god.model.trace.model;

import com.example.god.model.trace.util.GraphMap;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TrackTree {
    private RunTimeNode root;
    private RunTimeNode now;

    public TrackTree() {

    }

    public void addNewNode() {
        if (root == null) {
            root = new RunTimeNode();
            root.setChildren(new ArrayList<>());
            now = root;
        } else {
            RunTimeNode newNode = new RunTimeNode();
            newNode.setParent(now);
            newNode.setChildren(new ArrayList<>());
            now.getChildren().add(newNode);
            now = newNode;
        }
    }

    public void rollback() {
        if (now.getParent() != null) {
            now = now.getParent();
        }else{
            GraphMap.put(root.getClassName()+"."+root.getMethodName(),root);
            root.print();
        }
    }
}
