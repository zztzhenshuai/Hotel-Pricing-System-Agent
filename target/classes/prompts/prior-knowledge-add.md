# Attribute-Driven Design (ADD) Method

## Step 1 Review Inputs

The first step of the ADD method involves reviewing the inputs and identifying which requirements will be considered as architectural drivers.

## Step 2 Establish the Iteration Goal by Selecting Drivers

A design round generally takes the form of a series of design iterations, where each iteration focuses on achieving a particular goal. Such a goal typically involves designing to satisfy a subset of the drivers.

## Step 3 Choose One or More Elements of the System to Refine

This step is where the core design activities start. The elements that you will select are the ones that are involved in the satisfaction of specific drivers. For greenfield development, you can start by establishing the system context and then selecting the only available element--that is, the system itself--for refinement by decomposition. For existing systems or for later design iterations in greenfield systems, you would normally choose to refine elements that were identified in prior iterations.

## Step 4 Choose One or More Design Concepts That Satisfy the Selected Drivers

This step requires you to identify alternatives among design concepts that can be used to achieve your iteration goal, and to select one of these alternatives.

## Step 5 Instantiate Architectural Elements, Allocate Responsibilities, and Define Interfaces

This step requires instantiating architectural elements based on the selected design concepts and assigning responsibilities to these elements. The responsibilities should be allocated so that the selected drivers are satisfied. The interfaces among the elements should also be defined, including the information exchanged, the direction of interaction, and the dependencies among architectural elements.

## Step 6 Sketch Views and Record Design Decisions

This step requires producing architectural views that represent the current design. These views may include module views, component-and-connector views, allocation views, deployment views, or other appropriate views. The design decisions made during the iteration should also be recorded, including the selected design concepts, the architectural elements introduced, the responsibilities assigned to them, and the rationale for these decisions.

## Step 7 Perform Analysis of Current Design and Review Iteration Goal and Achievement of Design Purpose

This step requires analyzing the current design to determine whether the selected drivers and the iteration goal have been satisfied. The analysis should identify risks, trade-offs, unresolved issues, and design decisions that may need to be revisited. If the iteration goal has not been fully achieved, the results of the analysis should be used as input for subsequent iterations.
