package com.assignment.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PriorKnowledgeLoader {

    public String loadAddMethod() {
        return load("prompts/prior-knowledge-add.md");
    }

    public String loadHpsCase() {
        return load("prompts/prior-knowledge-hps.md");
    }

    private String load(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prior knowledge from: " + path, e);
        }
    }
}
