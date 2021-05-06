package com.example.god.model.trace.util;


import com.example.god.model.trace.model.KoTimeConfig;

public class Context {

    private static KoTimeConfig config;
    static {
        config = new KoTimeConfig();
        config.setLogEnable(false);
        config.setLogLanguage("chinese");
    }

    public static void setConfig(KoTimeConfig koTimeConfig) {
        config = koTimeConfig;
    }

    public static KoTimeConfig getConfig() {
        return config ;
    }

}
