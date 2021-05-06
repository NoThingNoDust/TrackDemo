package com.example.god.model.trace.util;

import com.example.god.model.trace.model.RunTimeNode;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

public class Common {
    public static Logger log = Logger.getLogger(Common.class.toString());

    public static StackTraceElement filter(StackTraceElement[] stacks,String packName) {
        String[] packNameSplit = packName.split("\\.");
        String filter = packNameSplit.length>1 ? packNameSplit[0]+"."+packNameSplit[1] : packNameSplit[0];
        int stacksLength = stacks.length;
        for (int i = 0; i < stacksLength; i++) {
            StackTraceElement stack = stacks[i];
            if (stack.getClassName().startsWith(filter)&& !stack.getClassName().contains("$")) {
                return stack;
            }
        }
        return null;
    }


    public static MethodType getMethodType(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class)!=null || targetClass.getAnnotation(RestController.class)!=null) {
            return MethodType.Controller;
        }else if (targetClass.getAnnotation(Service.class)!=null) {
            return MethodType.Service;
        }else if (targetClass.getAnnotation(Repository.class)!=null) {
            return MethodType.Dao;
        }
        String className = pjp.getThis().getClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        }else if (className.contains("service")) {
            return MethodType.Service;
        }else if (className.contains("dao") || className.contains("mapper")|| className.contains( "com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        }else{
            return MethodType.Others;
        }
    }
    public static MethodType getMethodType(ProceedingJoinPoint pjp) {
        Class<?> targetClass = pjp.getTarget().getClass();
        if (targetClass.getAnnotation(Controller.class)!=null || targetClass.getAnnotation(RestController.class)!=null) {
            return MethodType.Controller;
        }else if (targetClass.getAnnotation(Service.class)!=null) {
            return MethodType.Service;
        }else if (targetClass.getAnnotation(Repository.class)!=null) {
            return MethodType.Dao;
        }
        String className = pjp.getTarget().getClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        }else if (className.contains("service")) {
            return MethodType.Service;
        }else if (className.contains("dao") || className.contains("mapper")|| className.contains( "com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        }else{
            return MethodType.Others;
        }
    }

    public static MethodType getMethodType(String className) {
        className = className.toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        }else if (className.contains("service")) {
            return MethodType.Service;
        }else if (className.contains("dao") || className.contains("mapper")|| className.contains( "com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        }else{
            return MethodType.Others;
        }
    }

    public static void showLog(RunTimeNode current) {
        String currentKey = current.getClassName()+"."+current.getMethodName();
        if (Context.getConfig().getLogEnable() && "chinese".equals(Context.getConfig().getLogLanguage())) {
            log.info("调用方法="+currentKey+"，耗时="+current.getAvgRunTime()+"毫秒");
        }else if (Context.getConfig().getLogEnable() && "english".equals(Context.getConfig().getLogLanguage())) {
            log.info("method="+currentKey+"，runTime="+current.getAvgRunTime()+"ms");
        }
    }

}
