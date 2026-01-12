# Design: HTTP SSE Transport

## Architecture

The application will shift from a single-threaded CLI tool to a multi-client web server.

### 1. Connection Lifecycle (Per-Client)

Unlike Stdio (single session), HTTP is stateless, but MCP is stateful. We will manage sessions manually.

1.  **Connect (`GET /sse`)**:
    -   Client initiates connection.
    -   Server generates a unique `sessionId` (or uses one provided by the `SpringMcpTransportProvider`).
    -   Server creates an `SseEmitter`.
    -   Server creates a new `SseMcpTransport` and uses the `McpServer`'s session factory (via `McpServerTransportProvider`) to create a new session.
    -   Server sends the `endpoint` event pointing to `/messages?sessionId={id}`.
    -   Server stores the session in a `ConcurrentHashMap` within the transport provider.

2.  **Message (`POST /messages`)**:
    -   Client sends JSON-RPC to `/messages` with `sessionId`.
    -   Server looks up the session in the transport provider.
    -   Server handles the message via the retrieved session.
    -   `McpServer` (singleton) processes the message within the session context and sends responses back via the session's `SseTransport` -> `SseEmitter`.

3.  **Disconnect**:
    -   If `SseEmitter` completes or times out, the session is removed from the map.

### 2. Components

#### `McpController`
The entry point for Spring MVC.
-   `@GetMapping("/sse")`: Handles the handshake.
-   `@PostMapping("/messages")`: Routes incoming messages.

#### `SseMcpTransport`
A custom implementation of the MCP SDK's `ServerTransport` interface (if applicable) or a bridge class.
-   **Input:** Receives string/JSON from the Controller (POST body).
-   **Output:** Writes string/JSON to the `SseEmitter` (as SSE events).

### 3. Concurrency
-   Uses Spring's default threaded web server (Tomcat).
-   Session map must be thread-safe.

### 4. Configuration Changes
-   **Dependencies:** Add `spring-boot-starter-web`.
-   **Properties:** None mandatory, but `server.port` defaults to 8080.

## Trade-offs
-   **State Management:** We move from implicit state (process lifetime) to explicit state (session map). This introduces memory overhead per connected client.
-   **Complexity:** HTTP/SSE is inherently more complex than piping Stdio.
