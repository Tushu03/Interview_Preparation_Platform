package com.nt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DebugConfig {

	@Value("${spring.ai.vertex.project-id:NOT_FOUND}")
    private String projectId;

    @PostConstruct
    public void debug() {
        System.out.println(">>>> spring.ai.vertex.project-id = " + projectId);
    }
}
