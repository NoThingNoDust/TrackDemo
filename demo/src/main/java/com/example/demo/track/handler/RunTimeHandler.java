package com.example.demo.track.handler;

import com.example.demo.track.model.RunTimeNode;
import com.example.demo.track.model.TrackTree;
import com.example.demo.track.model.TrackTreePool;
import com.example.demo.track.util.Common;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class RunTimeHandler implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TrackTree trackTree = TrackTreePool.getTrackTree();

        long begin = System.nanoTime();
        trackTree.addNewNode();

        RunTimeNode now = trackTree.getNow();
        now.setPackageName(invocation.getThis().getClass().getPackage().getName());
        Object obj = invocation.proceed();

        long end = System.nanoTime();
        String className = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
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
