package com.example.god.model.process.formwork;

import com.example.god.model.process.formwork.model.ExecuteInfo;
import com.example.god.model.process.formwork.model.ProcessState;

/**
 * 事务生命周期
 */
public abstract class ProcessLifeCycle {
    protected ProcessState processState = ProcessState.READY;

    /**
     * 创建
     */
    public abstract void create(ExecuteInfo executeInfo);

    /**
     * 子调用
     */
    public abstract void call(ExecuteInfo executeInfo);

    /**
     * 返回父级
     */
    public abstract void ret(ExecuteInfo executeInfo);

    /**
     * 结束
     */
    public abstract void destroy(ExecuteInfo executeInfo);

    /**
     * 异常处理
     */
    public abstract void error(ExecuteInfo executeInfo);
}