# Switch to HTTP SSE Transport

## Summary
Replace the current Standard Input/Output (Stdio) transport with an HTTP-based transport using Server-Sent Events (SSE) for server-to-client messages and POST requests for client-to-server messages.

## Motivation
- **Remote Accessibility:** Stdio restricts the server to local execution by a parent process. HTTP allows the server to run as a standalone service accessible over a network.
- **Standard Compliance:** SSE is the standard transport mechanism defined by the Model Context Protocol for HTTP communication.
- **UI Integration:** An HTTP server allows serving a human-facing UI alongside the machine-facing MCP API on the same port.

## Scope
- **Configuration:** Update Maven dependencies to include Spring Web.
- **Components:**
    - Remove `StdioRunner`.
    - Create `McpConfig` to manage tool registration.
    - Create `McpController` to handle `/sse` and `/messages` endpoints.
    - Implement `SseServerTransport` (or equivalent) to bridge Spring's `SseEmitter` with the MCP SDK.
- **Specs:**
    - `http-server`: Update transport requirements.
    - `mcp-protocol`: Update initialization scenario.
