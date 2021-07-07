package com.example.god.model.clazz;

import java.util.Collection;

/**
 * Model of SearchMethodCommand
 * @author gongdewei 2020/4/9
 */
public class SearchMethodModel {
    private MethodVO methodInfo;
    private boolean detail;

    private Collection<ClassLoaderVO> matchedClassLoaders;
    private String classLoaderClass;

    public SearchMethodModel() {
    }

    public SearchMethodModel(MethodVO methodInfo, boolean detail) {
        this.methodInfo = methodInfo;
        this.detail = detail;
    }

    public MethodVO getMethodInfo() {
        return methodInfo;
    }

    public void setMethodInfo(MethodVO methodInfo) {
        this.methodInfo = methodInfo;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public String getClassLoaderClass() {
        return classLoaderClass;
    }

    public SearchMethodModel setClassLoaderClass(String classLoaderClass) {
        this.classLoaderClass = classLoaderClass;
        return this;
    }

    public Collection<ClassLoaderVO> getMatchedClassLoaders() {
        return matchedClassLoaders;
    }

    public SearchMethodModel setMatchedClassLoaders(Collection<ClassLoaderVO> matchedClassLoaders) {
        this.matchedClassLoaders = matchedClassLoaders;
        return this;
    }
}
