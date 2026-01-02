# Proposal: Check File Tool

## Context
The user needs a way to validate file content and placement *before* writing to disk. This is critical for enforcing FPF governance rules (like ensuring `type: hypothesis` files go into `30_Laboratory/Hypotheses`).

## Objective
Implement a `check_file` tool that accepts a file path and content string. It will run the content against a registry of configured checks (starting with RULE-001 for location validation) and return a success/failure result.

## Scope
-   New tool: `check_file` (implemented via `McpTool`).
-   New concept: `CheckRegistry` and `CheckRule`.
-   One implemented rule: `RULE-001` (Location Check).
-   Validation logic must be purely in-memory (no disk IO).
