package com.assignment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(CompatibleChatProperties.class)
public class AiConfig {

    @Bean
    @Primary
    public ChatModel compatibleChatModel(CompatibleChatProperties properties, ObjectMapper objectMapper) {
        return new CompatibleChatModel(properties, objectMapper);
    }
}
