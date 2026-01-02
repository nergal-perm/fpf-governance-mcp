# Design: Stub Tool Implementation

## Problem
The current `Main.kt` only handles `initialize`. We need to handle `tools/list` and `tools/call` without introducing a heavy JSON-RPC library yet, keeping the "stub" nature.

## Solution
Refactor the main loop to delegate tool operations to a registry of `McpTool` instances.

### Components
1.  **Tool Interface**: Define a simple contract (`McpTool`) for tools to expose their metadata (name, schema) and execution logic.
2.  **Tool Registry**: A simple in-memory map (`Map<String, McpTool>`) within the main application scope.
3.  **Main Loop Refactoring**:
    -   `tools/list` iterates over registered tools.
    -   `tools/call` looks up the tool by name and invokes it.
4.  **Echo Plugin**: Implement the `echo` tool as the first concrete implementation of `McpTool`.

### Trade-offs
-   **Regex Parsing**: Fragile for complex arguments, but sufficient for a controlled `echo` stub.
-   **Scalability**: This is still a temporary stub. The architecture will be refactored to a proper Clean Architecture + JSON-RPC library later.
