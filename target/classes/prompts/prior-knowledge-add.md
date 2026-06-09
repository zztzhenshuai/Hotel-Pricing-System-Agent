# Attribute-Driven Design (ADD) 3.0 Method

## Overview
ADD 3.0 is an architecture design method driven by quality attributes, constraints, and architectural concerns. It proceeds through iterative rounds, each consisting of seven steps.

## Step 1: Review Inputs
The first step of the ADD method involves reviewing the inputs and identifying which requirements will be considered as architectural drivers. Inputs include:
- Design Purpose
- Primary Functionality (Use Cases)
- Quality Attribute Scenarios
- Architectural Concerns
- Constraints

## Step 2: Establish the Iteration Goal by Selecting Drivers
A design round generally takes the form of a series of design iterations, where each iteration focuses on achieving a particular goal. Such a goal typically involves designing to satisfy a subset of the drivers. The architect selects the most important drivers for this iteration based on priority (importance and difficulty).

## Step 3: Choose One or More Elements of the System to Refine
This step is where the core design activities start. The elements that you will select are the ones that are involved in the satisfaction of specific drivers.
- For greenfield development: start by establishing the system context, then select the system itself for refinement by decomposition.
- For existing systems or later iterations: choose elements identified in prior iterations.

## Step 4: Choose One or More Design Concepts That Satisfy the Selected Drivers
This step requires you to identify alternatives among design concepts that can be used to achieve your iteration goal, and to select one of these alternatives. Design concepts include:
- Architectural patterns (e.g., layered, microservices, event-driven, pipe-and-filter)
- Tactics for quality attributes (e.g., replication for availability, caching for performance)
- Reference architectures
- Externally developed components and frameworks

## Step 5: Instantiate Architectural Elements, Allocate Responsibilities, and Define Interfaces
This step requires:
- Instantiating architectural elements based on the selected design concepts
- Assigning responsibilities to each element
- Defining interfaces between elements (data exchanged, protocols used)

## Step 6: Sketch Views and Record Design Decisions
This step involves:
- Producing architectural views (module view, component-and-connector view, deployment view, etc.)
- All views MUST be expressed as Mermaid or PlantUML code
- Recording the rationale for each key design decision
- Documenting what drivers each decision addresses

## Step 7: Perform Analysis of Current Design and Review Iteration Goal and Achievement of Design Purpose
This step involves:
- Reviewing whether the iteration goal has been achieved
- Identifying any not-yet-addressed drivers
- Determining whether another iteration is needed
- Updating the design backlog

## Key Terminology
- **Driver**: A requirement (functional, quality attribute, constraint, or concern) that significantly influences the architecture
- **Architectural element**: A software component, connector, or configuration element with a defined role in the architecture
- **View**: A representation of the architecture from a specific perspective
- **Tactic**: A design decision that directly addresses a quality attribute response
- **Pattern**: A recurring architectural solution with known trade-offs
