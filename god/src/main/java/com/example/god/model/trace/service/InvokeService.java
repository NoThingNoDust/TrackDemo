package com.example.god.model.trace.service;


import com.example.god.model.process.tree.model.RunTimeNode;
import com.example.god.model.trace.util.Common;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class InvokeService {
    public static Logger log = Logger.getLogger(InvokeService.class.toString());

    public static RunTimeNode getParentRunTimeNode(String packName) {
        String parentClassName = "";
        String parentMothodName = "";
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement stack = Common.filter(stacks, packName);
        if (stack != null) {
            parentClassName = stack.getClassName();
            parentMothodName = stack.getMethodName();
        }
        RunTimeNode parent = new RunTimeNode();
        parent.getMethodInfo().setClassName(parentClassName);
        parent.getMethodInfo().setMethodName(parentMothodName);
        //  parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".")+1)+"."+parentMothodName);
        parent.getMethodInfo().setMethodType(Common.getMethodType(parentClassName));
        parent.setChildren(new ArrayList<>());
        return parent;
    }

    public static RunTimeNode getCurrentRunTimeNode(MethodInvocation pjp, Double runTime) {
        String className = pjp.getThis().getClass().getName();
        String methodName = pjp.getMethod().getName();
        RunTimeNode current = new RunTimeNode();
        //  current.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        current.getMethodInfo().setClassName(className);
        current.getMethodInfo().setMethodName(methodName);
        current.getExecuteTime().setAvgRunTime(runTime);
        current.setChildren(new ArrayList<>());
        current.getMethodInfo().setMethodType(Common.getMethodType(pjp));
        return current;
    }

    public static RunTimeNode getCurrentRunTimeNode(ProceedingJoinPoint pjp, Double runTime) {
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        RunTimeNode current = new RunTimeNode();
        //   current.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        current.getMethodInfo().setClassName(className);
        current.getMethodInfo().setMethodName(methodName);
        current.getExecuteTime().setAvgRunTime(runTime);
        current.setChildren(new ArrayList<>());
        current.getMethodInfo().setMethodType(Common.getMethodType(pjp));
        return current;
    }

    public static void createGraph(RunTimeNode parent, RunTimeNode current) {
        if (current.getMethodInfo().getMethodName().contains("$")) {
            return;
        }
        Common.showLog(current);
        String parentKey = parent.getMethodInfo().getClassName() + "." + parent.getMethodInfo().getMethodName();
        String currentKey = current.getMethodInfo().getClassName() + "." + current.getMethodInfo().getMethodName();
        if (".".equals(parentKey)) {
            RunTimeNodeService.addOrUpdate(currentKey, current);
        } else if (RunTimeNodeService.containsNode(parent)) {
            RunTimeNodeService.addOrUpdateChildren(parent, current);
        } else {
            List<RunTimeNode> list = new ArrayList<>();
            list.add(current);
            parent.setChildren(list);
            RunTimeNodeService.add(parentKey, parent);
        }
    }

}
