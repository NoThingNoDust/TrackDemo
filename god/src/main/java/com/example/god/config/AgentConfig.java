package com.example.god.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AgentConfig {

    @Value("${god.agent.path:/Users/liule/Documents/agent/agent-0.0.2.jar}")
    private String agentPath;

    public String getAgentPath() {
        return agentPath;
    }
}
