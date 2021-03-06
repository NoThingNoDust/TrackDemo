package com.example.god.model.process.formwork.model;

/**
 * 生命周期状态
 */
public enum ProcessState {
    /**
     * 准备就绪
     */
    READY,
    /**
     * 运行中
     */
    RUNNING,
    /**
     * 结束
     */
    STOP,
    /**
     * 异常
     */
    ERROR
}