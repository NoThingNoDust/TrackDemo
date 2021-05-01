package com.example.god.model.jvm;

import java.util.*;

public class JvmModel {

    private final Map<String, List<JvmInfo>> jvmInfo;

    public JvmModel() {
        jvmInfo = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public JvmModel generator(String group, String name, Object value) {
        this.generator(group, name, value, null);
        return this;
    }

    public JvmModel  generator(String group, String name, Object value, String desc) {
        this.group(group).add(new JvmInfo(name, value, desc));
        return this;
    }

    public List<JvmInfo> group(String group) {
        synchronized (this) {
            return jvmInfo.computeIfAbsent(group, k -> new ArrayList<JvmInfo>());
        }
    }

    public Map<String, List<JvmInfo>> getJvmInfo() {
        return jvmInfo;
    }

}
