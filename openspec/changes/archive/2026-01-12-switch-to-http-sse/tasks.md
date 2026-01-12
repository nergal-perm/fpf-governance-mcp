# Tasks

1.  **Dependencies**
    -   [ ] Add `spring-boot-starter-web` to `pom.xml`.
    -   [ ] Remove `StdioRunner.java` to prevent CLI locking.

2.  **Transport Implementation**
    -   [ ] Create `src/main/java/org/fpf/governance/web/SseMcpTransport.java`.
        -   Implement logic to wrap `SseEmitter`.
        -   Provide method to accept incoming messages (from POST).

3.  **Controller Implementation**
    -   [ ] Create `src/main/java/org/fpf/governance/web/McpController.java`.
        -   Implement `GET /sse`.
        -   Implement `POST /messages`.
        -   Manage `Map<String, McpServer>` sessions.
        -   Inject `List<ToolSpecification>` to pass to new server instances.

4.  **Verification**
    -   [ ] Start the application.
    -   [ ] Use `curl -N http://localhost:8080/sse` to verify connection and receive session ID.
    -   [ ] Use `curl -X POST http://localhost:8080/messages?sessionId=...` to call `tools/list`.
    -   [ ] Verify proper JSON-RPC response in the SSE stream.
