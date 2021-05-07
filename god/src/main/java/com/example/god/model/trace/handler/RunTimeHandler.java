package com.example.god.model.trace.handler;

import com.example.god.model.trace.model.RunTimeNode;
import com.example.god.model.trace.model.TrackTree;
import com.example.god.model.trace.model.TrackTreePool;
import com.example.god.model.trace.util.Common;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

public class RunTimeHandler implements MethodInterceptor {

    private static final List<String> filterMethods = Arrays.asList("init", "doFilter", "destroy");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> clazz = invocation.getThis().getClass();
        //获取类名
        String className = clazz.getName();
        //获取方法名
        String methodName = invocation.getMethod().getName();
        //跳过Bean实例化
        if (className.contains("$")) {
            return invocation.proceed();
        }
        //跳过拦截器
        if (filterMethods.contains(methodName)) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface.getName().equals(Filter.class.getName())) {
                    return invocation.proceed();
                }
            }
        }

        TrackTree trackTree = TrackTreePool.getTrackTree();

        //开始计时
        long begin = System.nanoTime();
        trackTree.addNewNode();

        RunTimeNode now = trackTree.getNow();

        Object obj = invocation.proceed();

        //结束计时
        long end = System.nanoTime();
        now.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        now.setClassName(className);
        now.setMethodName(methodName);
        now.setAvgRunTime((end - begin) / 1000000.0);
        now.setMethodType(Common.getMethodType(invocation));
        now.setValue(now.getAvgRunTime());
        trackTree.rollback();
//
//        String packName = invocation.getThis().getClass().getPackage().getName();
//        RunTimeNode parent = InvokeService.getParentRunTimeNode(packName);
//        RunTimeNode current = InvokeService.getCurrentRunTimeNode(invocation, ((end - begin) / 1000000.0));
//        InvokeService.createGraph(parent, current);
        return obj;
    }
}
