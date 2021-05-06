package com.example.god.model.trace.model;

public class TrackTreePool {
    private final static ThreadLocal<TrackTree> TRACK_TREE_THREAD_LOCAL = new ThreadLocal<>();

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
