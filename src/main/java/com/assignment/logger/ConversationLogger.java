package com.assignment.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConversationLogger {

    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final DateTimeFormatter FILE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final List<Map<String, Object>> entries = new ArrayList<>();
    private final ObjectMapper mapper;
    private final String logDir;
    private final LocalDateTime sessionStart;
    private int totalPromptTokens;
    private int totalCompletionTokens;
    private int totalTokens;

    public ConversationLogger(@Value("${add.output.log-dir:logs}") String logDir) {
        this.logDir = logDir;
        this.sessionStart = LocalDateTime.now();
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        new File(logDir).mkdirs();
    }

    public void logSystem(String content) {
        addEntry("system", 0, content);
    }

    public void logUser(int iteration, String content) {
        addEntry("user", iteration, content);
    }

    public void logAssistant(int iteration, String content) {
        addEntry("assistant", iteration, content);
    }

    public void logAssistant(int iteration, String content, Usage usage) {
        Map<String, Object> entry = addEntry("assistant", iteration, content);
        if (usage != null) {
            Map<String, Object> tokenUsage = toTokenUsageMap(usage);
            entry.put("token_usage", tokenUsage);
            accumulateTokenUsage(tokenUsage);
        }
    }

    public void logInfo(String message) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FMT));
        entry.put("type", "info");
        entry.put("message", message);
        entries.add(entry);
        System.out.println("[INFO] " + message);
    }

    private Map<String, Object> addEntry(String role, int iteration, String content) {
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("timestamp", LocalDateTime.now().format(TIMESTAMP_FMT));
        entry.put("role", role);
        entry.put("iteration", iteration);
        entry.put("content", content);
        entries.add(entry);
        return entry;
    }

    public String exportToJson() {
        String filename = logDir + "/conversation-" +
                sessionStart.format(FILE_FMT) + ".json";

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("session_start", sessionStart.format(TIMESTAMP_FMT));
        output.put("session_end", LocalDateTime.now().format(TIMESTAMP_FMT));
        output.put("assignment", "Software Architecture (2026) Assignment 2");
        output.put("method", "ADD 3.0");
        output.put("ai_paradigm", "Single-Agent (Sequential reasoning + self-reflection)");
        output.put("llm", "gpt-5.4");
        output.put("total_turns", countHumanTurns());
        output.put("token_usage", sessionTokenUsage());
        output.put("messages", entries);

        try {
            mapper.writeValue(new File(filename), output);
            System.out.println("\n[LOG] Conversation saved: " + filename);
            return filename;
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save log: " + e.getMessage());
            return null;
        }
    }

    public void printSummary() {
        long userTurns = entries.stream()
                .filter(e -> "user".equals(e.get("role"))).count();
        long assistantTurns = entries.stream()
                .filter(e -> "assistant".equals(e.get("role"))).count();

        System.out.println("\n========== SESSION SUMMARY ==========");
        System.out.println("Total user turns    : " + userTurns);
        System.out.println("Total assistant turns: " + assistantTurns);
        System.out.println("Prompt tokens       : " + totalPromptTokens);
        System.out.println("Completion tokens   : " + totalCompletionTokens);
        System.out.println("Total tokens        : " + totalTokens);
        System.out.println("Session start       : " + sessionStart.format(TIMESTAMP_FMT));
        System.out.println("Session end         : " + LocalDateTime.now().format(TIMESTAMP_FMT));
        System.out.println("=====================================\n");
    }

    private long countHumanTurns() {
        return entries.stream()
                .filter(e -> "user".equals(e.get("role"))).count();
    }

    private Map<String, Object> toTokenUsageMap(Usage usage) {
        Map<String, Object> tokenUsage = new LinkedHashMap<>();
        tokenUsage.put("prompt_tokens", safeTokenCount(usage.getPromptTokens()));
        tokenUsage.put("completion_tokens", safeTokenCount(usage.getCompletionTokens()));
        tokenUsage.put("total_tokens", safeTokenCount(usage.getTotalTokens()));
        return tokenUsage;
    }

    private void accumulateTokenUsage(Map<String, Object> tokenUsage) {
        totalPromptTokens += (Integer) tokenUsage.get("prompt_tokens");
        totalCompletionTokens += (Integer) tokenUsage.get("completion_tokens");
        totalTokens += (Integer) tokenUsage.get("total_tokens");
    }

    private Map<String, Object> sessionTokenUsage() {
        Map<String, Object> tokenUsage = new LinkedHashMap<>();
        tokenUsage.put("prompt_tokens", totalPromptTokens);
        tokenUsage.put("completion_tokens", totalCompletionTokens);
        tokenUsage.put("total_tokens", totalTokens);
        tokenUsage.put("total_k_tokens", totalTokens / 1000.0);
        return tokenUsage;
    }

    private int safeTokenCount(Integer tokenCount) {
        return tokenCount == null ? 0 : tokenCount;
    }
}
