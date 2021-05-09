package com.example.god.model.trace.handler;

import com.example.god.model.process.tree.model.RunTimeNode;
import com.example.god.model.process.tree.model.TrackTree;
import com.example.god.model.trace.model.TrackTreePool;
import com.example.god.model.trace.service.InvokeService;
import com.example.god.model.trace.util.Common;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.scheduling.annotation.Scheduled;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.*;

public class RunTimeHandler implements MethodInterceptor {

    private static final List<String> filterMethods = Arrays.asList("init", "doFilter", "destroy");
    //调用链map，维护一套调用链列表
    private static final Map<String, RunTimeNode> traceMap = new HashMap<>();


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> clazz = invocation.getThis().getClass();
        //获取类名
        String className = clazz.getName();
        //获取方法名
        Method method = invocation.getMethod();
        String methodName = method.getName();
        //跳过Bean实例化
        if (className.contains("$")) {
            return invocation.proceed();
        }
        //跳过定时任务任务
        if (Objects.nonNull(method.getAnnotation(Scheduled.class))) {
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

        RunTimeNode the = new RunTimeNode();
        //     the.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        the.getMethodInfo().setClassName(className);
        the.getMethodInfo().setMethodName(methodName);
        the.getMethodInfo().setMethodType(Common.getMethodType(invocation));
        the.setChildren(new ArrayList<>());

        String thePath = className + "." + methodName;

        TrackTree trackTree = TrackTreePool.getTrackTree();

        String parentPath = TrackTreePool.getParent();
        //如果当前父路径为空，则表示此为初次进入
        if (parentPath == null) {
            parentPath = thePath;
            TrackTreePool.setParent(parentPath);
            traceMap.put(parentPath, the);
        } else {
            //非初次进入，则将父runTimeNode拿出来，然后处理。
            parentPath = getParentPath(className);
            RunTimeNode runTimeNode = traceMap.get(parentPath);
            List<RunTimeNode> children = runTimeNode.getChildren();
            children.add(the);
            traceMap.put(thePath, the);
        }

        //开始计时
        long begin = System.nanoTime();
        trackTree.addNewNode(null);

        RunTimeNode now = trackTree.getNow();

        Object obj = invocation.proceed();

        //结束计时
        long end = System.nanoTime();

        //塞入计时
        RunTimeNode runTimeNode = traceMap.get(thePath);
        runTimeNode.getExecuteTime().setAvgRunTime((end - begin) / 1000000.0);
        //       runTimeNode.setValue(runTimeNode.getAvgRunTime());

        //   now.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        now.getMethodInfo().setClassName(className);
        now.getMethodInfo().setMethodName(methodName);
        now.getExecuteTime().setAvgRunTime((end - begin) / 1000000.0);
        now.getMethodInfo().setMethodType(Common.getMethodType(invocation));
        //   now.setValue(now.getAvgRunTime());


        trackTree.rollback(null);
//
        String packName = invocation.getThis().getClass().getPackage().getName();
        RunTimeNode parent = InvokeService.getParentRunTimeNode(packName);
        RunTimeNode current = InvokeService.getCurrentRunTimeNode(invocation, ((end - begin) / 1000000.0));
        InvokeService.createGraph(parent, current);
        return obj;
    }

    /**
     * 获取当前类的父类+父方法名
     *
     * @param className 类名
     */
    private String getParentPath(String className) {
        //拼接上cjlib关键字
        String cglibTag = "$$EnhancerBySpringCGLIB$$";
        if (className.contains(cglibTag)) {
            return null;
        }
        className += cglibTag;
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            StackTraceElement stack = stacks[i];
            if (stack.getClassName().startsWith(className)) {
                StackTraceElement parent = stacks[i + 1];
                System.out.println(className + "parent=" + parent.getClassName() + "." + parent.getMethodName());
                //获取当当前类的父类名字和父类方法
                return parent.getClassName() + "." + parent.getMethodName();
            }
        }
        return null;
    }
}
