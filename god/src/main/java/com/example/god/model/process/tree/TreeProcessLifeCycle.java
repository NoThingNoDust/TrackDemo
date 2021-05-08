package com.example.god.model.process.tree;


import com.example.god.model.process.formwork.ProcessLifeCycle;
import com.example.god.model.process.formwork.model.ExecuteInfo;
import com.example.god.model.process.formwork.model.ProcessState;

/**
 * 树形事务生命周期
 */
public class TreeProcessLifeCycle extends ProcessLifeCycle {
    /**
     * 树形记录
     */
    private TrackTree trackTree;

    @Override
    public void create(ExecuteInfo executeInfo) {
        if (processState == ProcessState.READY) {
            trackTree = new TrackTree();
            processState = ProcessState.RUNNING;
        }
    }

    @Override
    public void call(ExecuteInfo executeInfo) {
        if (processState == ProcessState.RUNNING) {
            trackTree.addNewNode();
        }
    }

    @Override
    public void ret(ExecuteInfo executeInfo) {
        if (processState == ProcessState.RUNNING) {
            trackTree.rollback();
        }
    }

    @Override
    public void destroy(ExecuteInfo executeInfo) {
        if (processState == ProcessState.RUNNING) {
            processState = ProcessState.STOP;
            //todo 保存
        }
    }

    @Override
    public void error(ExecuteInfo executeInfo) {
        if (processState == ProcessState.RUNNING) {
            processState = ProcessState.ERROR;
            //todo 异常处理
        }
    }
}
