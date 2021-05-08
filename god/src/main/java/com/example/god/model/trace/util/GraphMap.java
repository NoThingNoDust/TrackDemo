package com.example.god.model.trace.util;

import com.example.god.model.process.tree.RunTimeNode;

import java.util.*;
import java.util.stream.Collectors;

public class GraphMap {

    /*只需保证可见性，无需保证线程安全*/
    private volatile static Map<String, RunTimeNode> runTimeNodeMap;

    static {
        runTimeNodeMap = new HashMap<>();
    }

    public static RunTimeNode get(String key) {
        return runTimeNodeMap.get(key);
    }

    public static RunTimeNode put(String key, RunTimeNode runTimeNode) {
        return runTimeNodeMap.put(key,runTimeNode);
    }

    public static boolean containsKey(String key) {
        return GraphMap.runTimeNodeMap.containsKey(key);
    }

    public static List<RunTimeNode> get(MethodType methodType) {
        return runTimeNodeMap.values().stream()
                .filter(runTimeNode -> runTimeNode.getMethodType()==methodType &&
                        !runTimeNode.getMethodName().contains("lambda$") )
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static RunTimeNode getTree(String key) {
        return runTimeNodeMap.get(key);
    }

    public static List<RunTimeNode> getAll() {
        return new ArrayList<>(runTimeNodeMap.values());
    }
}
