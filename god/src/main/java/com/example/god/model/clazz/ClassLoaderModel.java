package com.example.god.model.clazz;


import java.util.List;
import java.util.Collection;

/**
 * @author gongdewei 2020/4/21
 */
public class ClassLoaderModel {

    private ClassSetVO classSet;
    private List<String> resources;
    private ClassDetailVO loadClass;
    private List<String> urls;
    //classloader -l -t
    private List<ClassLoaderVO> classLoaders;
    private Boolean tree;

    private Collection<ClassLoaderVO> matchedClassLoaders;
    private String classLoaderClass;

    public ClassLoaderModel() {
    }

    public ClassSetVO getClassSet() {
        return classSet;
    }

    public ClassLoaderModel setClassSet(ClassSetVO classSet) {
        this.classSet = classSet;
        return this;
    }

    public List<String> getResources() {
        return resources;
    }

    public ClassLoaderModel setResources(List<String> resources) {
        this.resources = resources;
        return this;
    }

    public ClassDetailVO getLoadClass() {
        return loadClass;
    }

    public ClassLoaderModel setLoadClass(ClassDetailVO loadClass) {
        this.loadClass = loadClass;
        return this;
    }

    public List<String> getUrls() {
        return urls;
    }

    public ClassLoaderModel setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    public List<ClassLoaderVO> getClassLoaders() {
        return classLoaders;
    }

    public ClassLoaderModel setClassLoaders(List<ClassLoaderVO> classLoaders) {
        this.classLoaders = classLoaders;
        return this;
    }

    public Boolean getTree() {
        return tree;
    }

    public ClassLoaderModel setTree(Boolean tree) {
        this.tree = tree;
        return this;
    }

    public String getClassLoaderClass() {
        return classLoaderClass;
    }

    public ClassLoaderModel setClassLoaderClass(String classLoaderClass) {
        this.classLoaderClass = classLoaderClass;
        return this;
    }

    public Collection<ClassLoaderVO> getMatchedClassLoaders() {
        return matchedClassLoaders;
    }

    public ClassLoaderModel setMatchedClassLoaders(Collection<ClassLoaderVO> matchedClassLoaders) {
        this.matchedClassLoaders = matchedClassLoaders;
        return this;
    }
}
