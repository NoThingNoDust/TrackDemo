package com.example.god.model.trace.model;

import com.example.god.model.process.tree.model.TrackTree;

public class TrackTreePool {
    private final static ThreadLocal<TrackTree> TRACK_TREE_THREAD_LOCAL = new ThreadLocal<>();

    private final static ThreadLocal<String> TRACK_PARENT = new ThreadLocal<>();

    public static String getParent() {
        return TRACK_PARENT.get();
    }

    public static void setParent(String path) {
        TRACK_PARENT.set(path);
    }


    public static TrackTree getTrackTree() {
        if (TRACK_TREE_THREAD_LOCAL.get() == null) {
            TRACK_TREE_THREAD_LOCAL.set(new TrackTree());
        }
        return TRACK_TREE_THREAD_LOCAL.get();
    }

    public static void remove() {
        TRACK_TREE_THREAD_LOCAL.remove();
    }
}
