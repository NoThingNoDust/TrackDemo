package com.example.track.track.handler;

import com.example.track.track.model.RunTimeNode;
import com.example.track.track.service.InvokeService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class RunTimeHandler implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long begin = System.nanoTime();
        Object obj=invocation.proceed();
        long end =System.nanoTime();
        String packName = invocation.getThis().getClass().getPackage().getName();
        RunTimeNode parent = InvokeService.getParentRunTimeNode(packName);
        RunTimeNode current = InvokeService.getCurrentRunTimeNode(invocation,((end-begin)/1000000.0));
        InvokeService.createGraph(parent,current);
        return obj;
    }
}
