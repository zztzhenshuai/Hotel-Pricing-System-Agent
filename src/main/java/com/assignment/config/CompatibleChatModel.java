package com.assignment.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CompatibleChatModel implements ChatModel {

    private final CompatibleChatProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public CompatibleChatModel(CompatibleChatProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                .build();
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        try {
            ChatCompletionResponse response = exchange(prompt);
            Choice choice = firstChoice(response);
            String content = choice.message() == null ? "" : choice.message().content();

            Usage usage = new CompatibleUsage(response.usage());
            ChatResponseMetadata responseMetadata = ChatResponseMetadata.builder()
                    .id(response.id())
                    .model(response.model() == null ? properties.getModel() : response.model())
                    .usage(usage)
                    .build();
            ChatGenerationMetadata generationMetadata = ChatGenerationMetadata.builder()
                    .finishReason(choice.finishReason())
                    .build();

            return new ChatResponse(
                    List.of(new Generation(new AssistantMessage(content), generationMetadata)),
                    responseMetadata);
        } catch (IOException e) {
            throw new IllegalStateException("Compatible chat request failed.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Compatible chat request was interrupted.", e);
        }
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return ChatOptions.builder()
                .model(properties.getModel())
                .temperature(properties.getTemperature())
                .build();
    }

    private ChatCompletionResponse exchange(Prompt prompt) throws IOException, InterruptedException {
        ChatCompletionRequest requestBody = new ChatCompletionRequest(
                selectedModel(prompt),
                prompt.getInstructions().stream().map(this::toCompatibleMessage).toList(),
                selectedTemperature(prompt));

        String jsonBody = objectMapper.writeValueAsString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(chatCompletionsUri())
                .timeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                .header("Authorization", "Bearer " + properties.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
        return objectMapper.readValue(response.body(), ChatCompletionResponse.class);
    }

    private URI chatCompletionsUri() {
        String baseUrl = properties.getBaseUrl();
        if (!StringUtils.hasText(baseUrl)) {
            throw new IllegalStateException("app.ai.compatible.base-url must be configured.");
        }
        return URI.create(baseUrl.replaceAll("/+$", "") + "/v1/chat/completions");
    }

    private CompatibleMessage toCompatibleMessage(Message message) {
        String role = message.getMessageType().getValue().toLowerCase(Locale.ROOT);
        return new CompatibleMessage(role, message.getText());
    }

    private String selectedModel(Prompt prompt) {
        if (prompt.getOptions() != null && StringUtils.hasText(prompt.getOptions().getModel())) {
            return prompt.getOptions().getModel();
        }
        return properties.getModel();
    }

    private Double selectedTemperature(Prompt prompt) {
        if (prompt.getOptions() != null && prompt.getOptions().getTemperature() != null) {
            return prompt.getOptions().getTemperature();
        }
        return properties.getTemperature();
    }

    private Choice firstChoice(ChatCompletionResponse response) throws IOException {
        if (response.choices() == null || response.choices().isEmpty()) {
            throw new IOException("No choices returned by compatible chat endpoint.");
        }
        return response.choices().get(0);
    }

    private record ChatCompletionRequest(
            String model,
            List<CompatibleMessage> messages,
            Double temperature) {
    }

    private record CompatibleMessage(String role, String content) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatCompletionResponse(
            String id,
            String model,
            List<Choice> choices,
            TokenUsage usage) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(
            int index,
            CompatibleMessage message,
            @com.fasterxml.jackson.annotation.JsonProperty("finish_reason") String finishReason) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record TokenUsage(
            @com.fasterxml.jackson.annotation.JsonProperty("prompt_tokens") Integer promptTokens,
            @com.fasterxml.jackson.annotation.JsonProperty("completion_tokens") Integer completionTokens,
            @com.fasterxml.jackson.annotation.JsonProperty("total_tokens") Integer totalTokens) {
    }

    private record CompatibleUsage(TokenUsage nativeUsage) implements Usage {

        @Override
        public Integer getPromptTokens() {
            return nativeUsage == null || nativeUsage.promptTokens() == null ? 0 : nativeUsage.promptTokens();
        }

        @Override
        public Integer getCompletionTokens() {
            return nativeUsage == null || nativeUsage.completionTokens() == null ? 0 : nativeUsage.completionTokens();
        }

        @Override
        public Integer getTotalTokens() {
            return nativeUsage == null || nativeUsage.totalTokens() == null
                    ? getPromptTokens() + getCompletionTokens()
                    : nativeUsage.totalTokens();
        }

        @Override
        public Object getNativeUsage() {
            Map<String, Integer> usage = new LinkedHashMap<>();
            usage.put("prompt_tokens", getPromptTokens());
            usage.put("completion_tokens", getCompletionTokens());
            usage.put("total_tokens", getTotalTokens());
            return usage;
        }
    }
}
