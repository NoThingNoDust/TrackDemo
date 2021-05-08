package com.example.god.model.trace.service;

import com.example.god.model.process.tree.RunTimeNode;
import com.example.god.model.trace.model.SystemStatistic;
import com.example.god.model.trace.util.Context;
import com.example.god.model.trace.util.GraphMap;
import com.example.god.model.trace.util.MethodType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RunTimeNodeService {

    public static void addOrUpdate(String key, RunTimeNode runTimeNode) {
        if (GraphMap.containsKey(key)) {
            GraphMap.get(key).setAvgRunTime(runTimeNode.getAvgRunTime());
        }else{
            GraphMap.put(key,runTimeNode);
        }
    }

    public static void add(String key, RunTimeNode runTimeNode) {
        GraphMap.put(key,runTimeNode);
    }

    public static boolean containsKey(String key) {
       return GraphMap.containsKey(key);
    }

    public static boolean containsNode(RunTimeNode node) {
        String key = node.getClassName()+"."+node.getMethodName();
        return GraphMap.containsKey(key);
    }

    public static RunTimeNode getRunTimeNode(String key) {
        return GraphMap.get(key);
    }

    public static void addOrUpdateChildren(RunTimeNode parent, RunTimeNode current) {
        String parentKey = parent.getClassName()+"."+parent.getMethodName();
        RunTimeNode hisRunTimeNode = RunTimeNodeService.getRunTimeNode(parentKey);
        List<RunTimeNode> hisRunTimeNodeChildren = hisRunTimeNode.getChildren();
        if (hisRunTimeNodeChildren!=null) {
            if (hisRunTimeNodeChildren.contains(current)) {
                updateChildren(current,hisRunTimeNodeChildren);
            }else{
                hisRunTimeNodeChildren.add(current);
            }
        } else {
            List<RunTimeNode> list = new ArrayList<>();
            list.add(current);
            hisRunTimeNode.setChildren(list);
        }
        GraphMap.put(parentKey,hisRunTimeNode);
    }

    public static void updateChildren(RunTimeNode child, List<RunTimeNode> hisRunTimeNodeChildren) {
        int hisLength = hisRunTimeNodeChildren.size();
        for (int i = 0; i < hisLength; i++) {
            if (hisRunTimeNodeChildren.get(i)==child) {
                child.setAvgRunTime((child.getAvgRunTime()+hisRunTimeNodeChildren.get(i).getAvgRunTime())/2.0);
                hisRunTimeNodeChildren.set(i,child) ;
                break;
            }
        }
    }

    public static SystemStatistic getRunStatistic() {
        SystemStatistic systemStatistic = new SystemStatistic();
        List<RunTimeNode> controllerApis = GraphMap.get(MethodType.Controller);
        if (null==controllerApis || controllerApis.size()==0 ) {
            return systemStatistic;
        }
        int delayNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() >= Context.getConfig().getTimeThreshold()).count();
        systemStatistic.setDelayNum(delayNum);
        int normalNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() < Context.getConfig().getTimeThreshold()).count();
        systemStatistic.setNormalNum(normalNum);
        int totalNum = (int)controllerApis.stream().count();
        systemStatistic.setTotalNum(totalNum);
        Double max = controllerApis.stream().map(api->api.getAvgRunTime()).max(Double::compareTo).get();
        Double min = controllerApis.stream().map(api->api.getAvgRunTime()).min(Double::compareTo).get();
        Double avg = controllerApis.stream().map(api->api.getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }

    public static List<RunTimeNode> getControllers() {
        List<RunTimeNode> list = GraphMap.get(MethodType.Controller);
        return list;
    }

    /**
     * 根据methodName从GraphMap中查询RunTimeNode
     * @param methodName
     * @return
     */
    public static RunTimeNode getGraph(String methodName) {
        return GraphMap.getTree(methodName);
    }

    public static List<RunTimeNode> getAll() {
        return GraphMap.getAll();
    }
}
