# Proposal: Add Stub Tool

## Context
The server currently completes the `initialize` handshake but exposes no capabilities. To verify the full agentic loop (list tools -> call tool -> read result), we need a minimal working tool.

## Objective
## Objective
Establish a pluggable architecture for tools. Define a `McpTool` interface and registry, modify the main loop to support dynamic dispatch, and implement an `echo` tool as a proof-of-concept plugin.

## Scope
-   Update `initialize` response to advertise `tools` support.
-   Implement `tools/list` handler returning one tool (`echo`).
-   Implement `tools/call` handler executing `echo`.
-   Continue using the lightweight Stdio loop (raw JSON processing).
