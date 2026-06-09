package com.assignment;

import com.assignment.logger.ReportExtractor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * Alternative entry point: extract a Markdown report from an existing JSON log.
 *
 * Usage:
 *   SPRING_PROFILES_ACTIVE=extract \
 *   LOG_FILE=logs/conversation-20260611-093000.json \
 *   mvn spring-boot:run
 */
@SpringBootApplication
public class ExtractReportRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExtractReportRunner.class, args);
    }

    @Bean
    @Profile("extract")
    public CommandLineRunner extract(ReportExtractor extractor) {
        return args -> {
            String logFile = System.getenv("LOG_FILE");
            if (logFile == null || logFile.isBlank()) {
                System.err.println("Set LOG_FILE env variable to the JSON log path.");
                return;
            }
            extractor.extractToMarkdown(logFile);
        };
    }
}
