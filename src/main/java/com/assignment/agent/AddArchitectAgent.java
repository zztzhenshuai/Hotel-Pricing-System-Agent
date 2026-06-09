package com.assignment.agent;

import com.assignment.logger.ConversationLogger;
import com.assignment.prompt.IterationPrompts;
import com.assignment.prompt.SystemPromptBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddArchitectAgent {

    private static final int TOTAL_ITERATIONS = 4;

    private final ChatClient chatClient;
    private final ConversationLogger logger;
    private final SystemPromptBuilder promptBuilder;

    // Manually maintained conversation history for cross-iteration context
    private final List<Message> history = new ArrayList<>();

    public AddArchitectAgent(ChatClient chatClient,
                              ConversationLogger logger,
                              SystemPromptBuilder promptBuilder) {
        this.chatClient = chatClient;
        this.logger = logger;
        this.promptBuilder = promptBuilder;
    }

    public void runAllIterations() {
        String systemPrompt = promptBuilder.build();
        logger.logSystem(systemPrompt);
        logger.logInfo("Session started.");

        // Step 1 executed once before the iteration loop
        executeStep(0, "ADD Step 1: Review Inputs", IterationPrompts.getStep1(), systemPrompt);

        for (int i = 1; i <= TOTAL_ITERATIONS; i++) {
            executeStep(i, getIterationLabel(i), IterationPrompts.getIteration(i), systemPrompt);
        }

        logger.printSummary();
        logger.exportToJson();
    }

    private void executeStep(int iteration, String label, String userPrompt, String systemPrompt) {
        logger.logInfo("Starting: " + label);
        logger.logUser(iteration, userPrompt);
        printSeparator(label.toUpperCase());

        // Build message list: system + full history + new user message
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        messages.addAll(history);
        messages.add(new UserMessage(userPrompt));

        String response;
        try {
            response = chatClient.prompt(new Prompt(messages))
                    .call()
                    .content();
        } catch (Exception e) {
            response = "[ERROR] LLM call failed: " + e.getMessage();
            logger.logInfo(response);
            System.err.println(response);
        }

        // Append this round to history so subsequent iterations have full context
        history.add(new UserMessage(userPrompt));
        history.add(new AssistantMessage(response));

        logger.logAssistant(iteration, response);
        System.out.println(response);
        printDone(label);
    }

    private String getIterationLabel(int num) {
        return switch (num) {
            case 1 -> "Iteration 1: Establishing an Overall System Structure";
            case 2 -> "Iteration 2: Identifying Structures to Support Primary Functionality";
            case 3 -> "Iteration 3: Addressing Reliability and Availability Quality Attributes";
            case 4 -> "Iteration 4: Addressing Development and Operations";
            default -> "Iteration " + num;
        };
    }

    private void printSeparator(String title) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  " + title);
        System.out.println("=".repeat(70) + "\n");
    }

    private void printDone(String label) {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("  COMPLETED: " + label);
        System.out.println("-".repeat(70) + "\n");
    }
}
