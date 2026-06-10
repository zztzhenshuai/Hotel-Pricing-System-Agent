package com.assignment.prompt;

public final class IterationPrompts {

    private IterationPrompts() {}

    public static String getStep1() {
        return """
                ## ADD Step 1: Review Inputs

                Before beginning the iterative design, review all inputs for the
                Hotel Pricing System and identify the architectural drivers.

                Produce a structured summary of:
                1. The design purpose
                2. All primary use cases (HPS-1 through HPS-6)
                3. All quality attribute scenarios (QA-1 through QA-9), ranked by
                   importance and difficulty
                4. All architectural concerns (CRN-1 through CRN-5)
                5. All constraints (CON-1 through CON-6)
                6. A prioritized list of architectural drivers that will guide the
                   four design iterations

                Apply the self-reflection protocol after completing this step.
                """;
    }

    public static String getIteration(int iterationNum) {
        return switch (iterationNum) {
            case 1 -> """
                    ## Iteration 1: Establishing an Overall System Structure

                    Now execute ADD Steps 2 through 7 for this iteration.

                    Context: This is the first design iteration for the Hotel Pricing
                    System (a greenfield system). You are starting from scratch. The
                    element to be refined is the entire system.

                    Focus drivers for this iteration:
                    - CRN-1 (Establish overall system structure)
                    - CRN-2 (Leverage Java, Angular, Kafka)
                    - CON-1 (Web browser access, cross-platform)
                    - CON-2 (Cloud-hosted, cloud identity service)
                    - CON-6 (Cloud-native approach)
                    - QA-5 (Security: credential validation)

                    For Step 6, produce the following views as Mermaid or PlantUML:
                    a) Module decomposition view (top-level modules)
                    b) Initial deployment view (cloud infrastructure)

                    Apply the self-reflection protocol after each step (Steps 2–7).
                    """;

            case 2 -> """
                    ## Iteration 2: Identifying Structures to Support Primary Functionality

                    Now execute ADD Steps 2 through 7 for this iteration.

                    Context: Build upon the overall system structure established in
                    Iteration 1. Refine the internal structures of the identified
                    modules to support the primary use cases.

                    Focus drivers for this iteration:
                    - HPS-1 (Log In)
                    - HPS-2 (Change Prices)
                    - HPS-3 (Query Prices)
                    - HPS-4 (Manage Hotels)
                    - HPS-5 (Manage Rates)
                    - HPS-6 (Manage Users)
                    - QA-1 (Performance: price publication < 100 ms)
                    - CRN-3 (Allocate work to development team)

                    For Step 6, produce the following views as Mermaid or PlantUML:
                    a) Component-and-connector view showing how primary use cases flow
                       through the system components
                    b) Sequence diagram for HPS-2 (Change Prices) as the most complex
                       and performance-critical use case

                    Apply the self-reflection protocol after each step (Steps 2–7).
                    """;

            case 3 -> """
                    ## Iteration 3: Addressing Reliability and Availability Quality Attributes

                    Now execute ADD Steps 2 through 7 for this iteration.

                    Context: Build upon the structures from Iterations 1 and 2.
                    Select design concepts that satisfy the high-priority reliability,
                    availability, scalability, and modifiability drivers.

                    Focus drivers for this iteration:
                    - QA-2 (Reliability: 100% price change publication)
                    - QA-3 (Availability: 99.9% uptime SLA)
                    - QA-4 (Scalability: 100K–1M queries/day)
                    - QA-6 (Modifiability: support non-REST protocols without core changes)

                    For Step 6, produce the following views as Mermaid or PlantUML:
                    a) Updated C&C view showing the selected design concepts for
                       reliability and availability
                    b) Updated deployment view reflecting redundancy and scaling

                    Apply the self-reflection protocol after each step (Steps 2–7).
                    """;

            case 4 -> """
                    ## Iteration 4: Addressing Development and Operations

                    Now execute ADD Steps 2 through 7 for this iteration.

                    Context: Build upon all previous iterations. Address the concerns
                    and quality attributes related to how the system is developed,
                    tested, deployed, and operated.

                    Focus drivers for this iteration:
                    - QA-7 (Deployability: no code changes between environments)
                    - QA-8 (Monitorability: 100% metrics collection for price publication)
                    - QA-9 (Testability: integration testing independent of external systems)
                    - CRN-3 (Allocate work to development team)
                    - CRN-4 (Avoid technical debt)
                    - CRN-5 (Set up continuous deployment infrastructure)
                    - CON-3 (Git-based platform)
                    - CON-4 (6-month delivery, 2-month MVP)

                    For Step 6, produce the following views as Mermaid or PlantUML:
                    a) CI/CD pipeline view
                    b) Monitoring and observability view
                    c) Work allocation view (mapping modules to development team members)

                    Apply the self-reflection protocol after each step (Steps 2–7).

                    After completing Step 7, provide a final summary of the complete
                    architectural design covering all four iterations, including:
                    - Key architectural decisions made
                    - How each driver (QA, CON, CRN) was addressed
                    - Outstanding risks or not-yet-addressed concerns
                    """;

            default -> throw new IllegalArgumentException(
                    "Iteration number must be 1-4, got: " + iterationNum);
        };
    }
}
