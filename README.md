# TrackDemo

## `agent:`ASM的Agent：[参考美团ASM技术入门](https://tech.meituan.com/2019/09/05/java-bytecode-enhancement.html)

## `demo:`SpringBoot环境自测项目(可忽略)

## `god:`实现各种功能的核心SpringBoot工程：[参考阿里Arthas](https://arthas.aliyun.com/doc/advanced-use.html)

```
✅:已完成   🌓：进行中(月食进度表示开发进度)  ⛔️：待取舍
```

### `JvmController`：实现了JVM相关功能

thread——查看当前 JVM 的线程堆栈信息✅

jvm——查看当前 JVM 的信息✅

sysprop——查看和修改JVM的系统属性✅

sysenv——查看JVM的环境变量✅

vmoption——查看和修改JVM里诊断相关的option✅

heapdump——dump java heap, 类似jmap命令的heap dump功能✅

### `ClassController`：实现类加载类编译相关功能
sc——查看JVM已加载的类信息⛔️

sm——查看已加载类的方法信息⛔️️

jad——反编译指定已加载类的源码⛔️

mc——内存编译器，内存编译.java文件为.class文件🌓

retransform——加载外部的.class文件，retransform到JVM里🌓

redefine——加载外部的.class文件，redefine到JVM里🌓

dump——dump 已加载类的 byte code 到特定目录⛔️

classloader——查看classloader的继承树，urls，类加载信息，使用classloader去getResource⛔️

### `TraceController`：实现方法执行时间统计，调用链追踪相关功能

monitor——方法执行监控🌓

trace——方法调用链追踪和路径上执行时间统计🌓
1. 平均调用时间统计✅
2. 最长调用时间统计✅
3. 最近5次调用时间统计🌓


备注：前端页面展示参考https://gitee.com/huoyo/ko-time.git 
