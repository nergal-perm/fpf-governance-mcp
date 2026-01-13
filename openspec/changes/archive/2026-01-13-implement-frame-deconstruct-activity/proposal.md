# Proposal: Implement Frame & Deconstruct Activity

## Context
The "Frame & Deconstruct" activity is the first step in the FPF governance process. It allows users to dump vague ideas ("Brain Dump") and have the AI "Deconstruct" them using formal logic.

## Goal
Implement the user interface and backend logic to support this activity.

## Changes

### Frontend
-   Update the web interface to support a 2-column layout.
    -   **Left Column (1/3):** User input form (textarea).
    -   **Right Column (2/3):** Brain response display area.
        -   **Holons Table:** Render the `holons` array as a structured table.
        -   **Reframed Problem:** Display `reframedProblem` as standard text.
        -   **Category Errors:** Render `categoryErrors` as an 'error'-styled callout (red/warning colors).
        -   **Ghost Reinstantiations:** Render `ghostReinstantiations` as a 'warning'-styled callout (yellow/info colors).
-   Use HTMX for partial updates.

### Backend (Logic Engine)
-   Create a prompt template file in `src/main/resources` that contains the instructions for the `fpf:deconstruct` command.
-   The backend will load this template, inject the user's input, and send the final "enhanced" prompt to the `GeminiCliBrain`.
-   **JSON Extraction:** Since the model might include natural language or Markdown blocks (e.g., ```json ... ```) in its response, the backend must extract only the valid JSON structure before sending it to the frontend.
-   **Response Parsing:** The backend should parse the JSON to a strongly-typed Java object (DTO) to facilitate structured rendering in Thymeleaf, rather than passing raw JSON strings.
-   No changes to the `GeminiCliBrain` invocation are required.

## Impact
-   Enables the first core activity of the FPF Governance Agent.
-   Establishes a pattern for storing and using prompt templates from application resources.