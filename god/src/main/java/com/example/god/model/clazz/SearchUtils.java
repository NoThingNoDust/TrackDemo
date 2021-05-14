package com.example.god.model.clazz;

import org.springframework.util.StringUtils;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类搜索工具
 * @author diecui1202 on 2017/09/07.
 */
public class SearchUtils {

    /**
     * 根据类名匹配，搜索已经被JVM加载的类
     *
     * @param inst             inst
     * @param className        类名路径匹配
     * @return 匹配的类集合
     */
    public static Set<Class<?>> searchClass(Instrumentation inst, String className, String classLoaderHashCode) {
        String trans = trans(className);
        final Set<Class<?>> matches = new HashSet<>();
        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            if (trans.equals(clazz.getName())) {
                if (classLoaderHashCode == null) {
                    matches.add(clazz);
                } else {
                    if (Integer.toHexString(clazz.getClassLoader().hashCode()).equals(classLoaderHashCode)) {
                        matches.add(clazz);
                    }
                }
            }
            if (matches.size() >= Integer.MAX_VALUE) {
                break;
            }
        }
        return matches;
    }

    /**
     * 将 反斜/ 转化为 .
     * @param classNamePath 类全路径限定名
     */
    public static String trans(String classNamePath) {
        if (StringUtils.isEmpty(classNamePath)) {
            classNamePath = "*";
        }
        if (!classNamePath.contains("$$Lambda")) {
            classNamePath = StringUtils.replace(classNamePath, "/", ".");
        }
        return classNamePath;
    }
}
