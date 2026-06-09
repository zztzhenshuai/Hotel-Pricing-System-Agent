# Hotel Pricing System — ADD 3.0 Architecture Design Agent

**Software Architecture (2026) Assignment 2**

AI Paradigm: Single-Agent (Sequential reasoning + self-reflection)  
Framework: Spring Boot 3.3 + Spring AI

---

## Project Overview

This project implements a **single-agent** that uses an LLM to perform architectural design for the Hotel Pricing System (HPS) case study, following the ADD 3.0 method. The agent:

1. Loads prior knowledge (ADD 3.0 method + HPS case study) as a system prompt
2. Executes **ADD Step 1** (Review Inputs) once
3. Executes **Iterations 1–4**, each performing ADD Steps 2–7 in sequence
4. After every step, performs **self-reflection** (4 checks: view format, traceability, goal coverage, consistency)
5. Saves the full timestamped conversation to `logs/conversation-<datetime>.json`

---

## Prerequisites

- Java 17 or above
- Maven 3.8 or above
- An OpenAI-compatible API key and base URL (provided by the course TA)

---

## Configuration

Open `src/main/resources/application.yml` and fill in your credentials:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:your-api-key-here}
      base-url: ${OPENAI_BASE_URL:https://api.openai.com}
      chat:
        options:
          model: gpt-5.4
```

You can either edit the file directly, or set environment variables before running:

```bash
export OPENAI_API_KEY=<your-key>
export OPENAI_BASE_URL=<course-provided-url>
```

> **Note:** The `base-url` should NOT include `/v1` — Spring AI appends that automatically.  
> For example, use `https://example.com`, not `https://example.com/v1`.

> **Note:** The API key is provided solely for assignment execution. It must not be used for code generation or code-writing assistance.

---

## How to Run

### Step 1 — Build

```bash
mvn package -DskipTests
```

### Step 2 — Run the agent

```bash
java -jar target/hotel-pricing-add-1.0.0.jar
```

Or with Maven directly:

```bash
mvn spring-boot:run
```

The agent will run automatically with no user interaction required. When finished, it prints a summary and exits. The full conversation log is saved to:

```
logs/conversation-<datetime>.json
```

### Step 3 (Optional) — Extract a Markdown report skeleton

After the run completes, you can generate a Markdown skeleton from the log:

```bash
export LOG_FILE=logs/conversation-<datetime>.json
SPRING_PROFILES_ACTIVE=extract java -jar target/hotel-pricing-add-1.0.0.jar
```

This produces `logs/conversation-<datetime>-report.md`, pre-filled with all LLM responses, ready for editing into the final submission report.

---

## Project Structure

```
src/main/java/com/assignment/
├── HotelPricingAddApplication.java     # Entry point
├── ExtractReportRunner.java            # Report extraction runner
├── agent/
│   └── AddArchitectAgent.java          # Core agent: orchestrates ADD steps and iterations
├── config/
│   └── AiConfig.java                   # Spring AI ChatClient configuration
├── logger/
│   ├── ConversationLogger.java         # Saves timestamped conversation to JSON
│   └── ReportExtractor.java            # Converts JSON log to Markdown report
└── prompt/
    ├── SystemPromptBuilder.java         # Assembles the 3-section system prompt
    ├── IterationPrompts.java            # User prompts for Step 1 and Iterations 1–4
    └── PriorKnowledgeLoader.java        # Loads prior knowledge files from resources

src/main/resources/
├── application.yml                      # Main configuration (API key, model, etc.)
├── application-extract.yml             # Configuration for report extraction mode
└── prompts/
    ├── prior-knowledge-add.md           # ADD 3.0 method description (7 steps)
    └── prior-knowledge-hps.md           # HPS case study (use cases, QA, concerns, constraints)

logs/
└── conversation-<datetime>.json         # Output: full timestamped conversation log
```

---

## System Prompt Design

The system prompt is structured into three sections:

| Section | Content |
|---------|---------|
| Section 1: Prior Knowledge | ADD 3.0 method + HPS case study + Iteration Plan |
| Section 2: Role Prompt | Architect role + 6 strict rules (Mermaid/PlantUML only, no external knowledge, etc.) |
| Section 3: Self-Reflection Protocol | 4 checks after every step + required `[SELF-REFLECTION]` marker |

---

## Output Log Format

```json
{
  "session_start": "2026-06-09 20:02:54.751",
  "session_end":   "2026-06-09 20:10:31.678",
  "assignment":    "Software Architecture (2026) Assignment 2",
  "ai_paradigm":   "Single-Agent (Sequential reasoning + self-reflection)",
  "llm":           "gpt-5.4",
  "total_turns":   5,
  "messages": [
    {
      "timestamp": "2026-06-09 20:02:54.885",
      "role":      "user",
      "iteration": 0,
      "content":   "## ADD Step 1: Review Inputs ..."
    },
    {
      "timestamp": "2026-06-09 20:03:10.123",
      "role":      "assistant",
      "iteration": 0,
      "content":   "### ADD Step 1 Output ..."
    }
  ]
}
```

- `iteration: 0` — ADD Step 1 (pre-iteration review)
- `iteration: 1–4` — Iterations 1–4 (Steps 2–7 each)

---

## Deliverables

| Item | Points |
|------|--------|
| Source code (this project) | 15 pts |
| `logs/conversation-<datetime>.json` (complete log with timestamps) | 15 pts |
| Report (English, ≤ 30 pages A4, submitted to Moodle) | 20 pts |

Report sections:
- **Part I**: ADD output results for all 4 iterations (Step 1 + Steps 2–7 × 4)
- **Part II**: Interaction cost analysis (paradigm, LLM, turns, token consumption, time cost)
- **Part III**: Individual reflection + personal contributions per group member
