- [x] Update `pom.xml` with Spring Boot dependencies (`spring-boot-starter`). <!-- id: 0 -->
- [x] Create `GovernanceApplication.java` (Spring Boot Main). <!-- id: 1 -->
- [x] Create `McpConfig.java` to configure Services and Tools as Beans. <!-- id: 2 -->
- [x] Implement `StdioRunner` implementing `CommandLineRunner`. <!-- id: 3 -->
    *   Inject `List<ToolSpecification>`.
    *   Run `McpServer.sync` with `StdioServerTransport`.
- [x] Create `application.properties` with logging config (redirect logs to stderr) to keep stdout clean for JSON-RPC. <!-- id: 4 -->
- [x] Verify `mvn package` produces a working JAR. <!-- id: 5 -->
- [x] Verify the server works with an MCP client (or manual input) over stdio. <!-- id: 6 -->
