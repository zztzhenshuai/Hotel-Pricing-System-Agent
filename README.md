# Hotel Pricing System - ADD 3.0 Architecture Design Agent

**Software Architecture (2026) Assignment 2**

AI Paradigm: Single-Agent (Sequential reasoning + self-reflection)  
Framework: Spring Boot 3.3 + Spring AI Alibaba + Spring AI ChatModel  
Model: `gpt-5.4`

---

## Project Overview

This project implements a single-agent that uses an LLM to perform
architectural design for the Hotel Pricing System (HPS) case study by
following the ADD 3.0 method. The agent:

1. Loads prior knowledge (ADD 3.0 method + HPS case study) as a system prompt.
2. Executes ADD Step 1 (Review Inputs) once.
3. Executes Iterations 1-4, each performing ADD Steps 2-7 in sequence.
4. Performs self-reflection after every ADD step.
5. Saves the full timestamped conversation to `logs/conversation-<datetime>.json`.

The project keeps Spring AI Alibaba dependencies in the application stack. The
actual course endpoint is accessed through a local Spring AI `ChatModel`
implementation for the `/v1/chat/completions` compatible protocol. The
configured model name remains `gpt-5.4`.

---

## Prerequisites

- Java 17 or above
- Maven 3.8 or above
- A course-provided API key and `/v1/chat/completions` compatible model endpoint

---

## Configuration

Open `src/main/resources/application.yml` and configure the model endpoint:

```yaml
spring:
  ai:
    dashscope:
      api-key: ${AI_DASHSCOPE_API_KEY:your-api-key-here}
      base-url: ${AI_DASHSCOPE_BASE_URL:https://dashscope.aliyuncs.com/compatible-mode}
      chat:
        enabled: false
        options:
          model: gpt-5.4
          temperature: 0.3
app:
  ai:
    compatible:
      api-key: ${AI_DASHSCOPE_API_KEY:your-api-key-here}
      base-url: ${AI_DASHSCOPE_BASE_URL:https://yunwu.ai}
      model: gpt-5.4
      temperature: 0.3
      timeout-seconds: 180
```

You can set environment variables before running:

```bash
export AI_DASHSCOPE_API_KEY=<your-key>
export AI_DASHSCOPE_BASE_URL=<course-provided-url>
```

For PowerShell:

```powershell
$env:AI_DASHSCOPE_API_KEY="<your-key>"
$env:AI_DASHSCOPE_BASE_URL="<course-provided-url>"
```

The API key is provided solely for assignment execution and resource access.
It must not be used for code generation or code-writing assistance.

---

## How to Run

### Step 1 - Build

```bash
mvn package -DskipTests
```

### Step 2 - Run the Agent

```bash
java -jar target/hotel-pricing-add-1.0.0.jar
```

Or with Maven directly:

```bash
mvn spring-boot:run
```

The agent runs automatically with no interactive input. When finished, it
prints a summary and exits. The full conversation log is saved to:

```text
logs/conversation-<datetime>.json
```

### Step 3 - Extract a Markdown Report Skeleton

After the run completes, generate a Markdown skeleton from the log:

```bash
export LOG_FILE=logs/conversation-<datetime>.json
SPRING_PROFILES_ACTIVE=extract java -jar target/hotel-pricing-add-1.0.0.jar
```

For PowerShell:

```powershell
$env:LOG_FILE="logs\conversation-<datetime>.json"
$env:SPRING_PROFILES_ACTIVE="extract"
java -jar target\hotel-pricing-add-1.0.0.jar
```

This produces `logs/conversation-<datetime>-report.md`.

---

## Project Structure

```text
src/main/java/com/assignment/
|-- HotelPricingAddApplication.java     # Main entry point
|-- ExtractReportRunner.java            # Report extraction runner
|-- agent/
|   `-- AddArchitectAgent.java          # Core single-agent orchestration
|-- logger/
|   |-- ConversationLogger.java         # Timestamped JSON conversation log
|   `-- ReportExtractor.java            # JSON log to Markdown skeleton
`-- prompt/
    |-- SystemPromptBuilder.java        # System prompt assembly
    |-- IterationPrompts.java           # Step 1 and Iteration 1-4 prompts
    `-- PriorKnowledgeLoader.java       # Prior knowledge loader

src/main/resources/
|-- application.yml                     # Spring AI Alibaba DashScope config
|-- application-extract.yml             # Report extraction profile config
`-- prompts/
    |-- prior-knowledge-add.md          # ADD 3.0 method description
    `-- prior-knowledge-hps.md          # HPS case study

logs/
`-- conversation-<datetime>.json        # Timestamped conversation log
```

---

## Output Log Format

```json
{
  "session_start": "2026-06-09 20:02:54.751",
  "session_end": "2026-06-09 20:10:31.678",
  "assignment": "Software Architecture (2026) Assignment 2",
  "method": "ADD 3.0",
  "ai_paradigm": "Single-Agent (Sequential reasoning + self-reflection)",
  "llm": "gpt-5.4",
  "total_turns": 5,
  "token_usage": {
    "prompt_tokens": 0,
    "completion_tokens": 0,
    "total_tokens": 0,
    "total_k_tokens": 0.0
  },
  "messages": [
    {
      "timestamp": "2026-06-09 20:02:54.885",
      "role": "user",
      "iteration": 0,
      "content": "## ADD Step 1: Review Inputs ..."
    }
  ]
}
```

`iteration: 0` is ADD Step 1. `iteration: 1-4` are the four required ADD
design iterations.

---

## Deliverables

| Item | Points |
|------|--------|
| Source code | 15 pts |
| `logs/conversation-<datetime>.json` with timestamps | 15 pts |
| English report, no more than 30 A4 pages | 20 pts |

Report sections:

- Part I: ADD output results for Step 1 and all four iterations
- Part II: Interaction cost analysis
- Part III: Individual reflection and personal contribution
