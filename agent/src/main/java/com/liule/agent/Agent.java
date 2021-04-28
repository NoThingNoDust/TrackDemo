package com.liule.agent;

import jdk.internal.org.objectweb.asm.ClassReader;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void agentmain(String args, Instrumentation inst)
            throws Exception {
        // 注意不能仅仅根据路径来确定类名，而是要通过包中的路径+类名！！！
        String path = args;
        System.out.println("path = " + path);
        String targetClassName = getTargetClass(path).replace("/",".");
        System.out.println("targetClassName = " + targetClassName);
        inst.addTransformer(new RedefineTransformer(path,targetClassName), true);

        /**
         * 这段代码的意思是，重新转换目标类，也就是 Account 类。也就是说，你需要重新定义哪个类，需要指定，否则 JVM 不可能知道。
         * 还有一个类似的方法 redefineClasses ，注意，这个方法是在类加载前使用的。类加载后需要使用 retransformClasses 方法 */
        inst.retransformClasses(getClassByRedefinePath(targetClassName));
    }

    public static Class<?> getClassByRedefinePath(String targetClassName) throws Exception{
        return Class.forName(targetClassName);
    }
    // 通过asm来获取一个class文件的包名与类名x
    public static String getTargetClass(String path){
        System.out.println("获取开始====");
        byte[] bytes = RedefineTransformer.getBytesFromFile(path);
        System.out.println("获取成功====");
        ClassReader classReader = new ClassReader(bytes);
        return classReader.getClassName();
    }
}
