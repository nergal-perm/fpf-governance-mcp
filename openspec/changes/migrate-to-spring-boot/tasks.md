- [ ] Update `pom.xml` with Spring Boot dependencies (`spring-boot-starter`). <!-- id: 0 -->
- [ ] Create `GovernanceApplication.java` (Spring Boot Main). <!-- id: 1 -->
- [ ] Create `McpConfig.java` to configure Services and Tools as Beans. <!-- id: 2 -->
- [ ] Implement `StdioRunner` implementing `CommandLineRunner`. <!-- id: 3 -->
    *   Inject `List<ToolSpecification>`.
    *   Run `McpServer.sync` with `StdioServerTransport`.
- [ ] Create `application.properties` with logging config (redirect logs to stderr) to keep stdout clean for JSON-RPC. <!-- id: 4 -->
- [ ] Verify `mvn package` produces a working JAR. <!-- id: 5 -->
- [ ] Verify the server works with an MCP client (or manual input) over stdio. <!-- id: 6 -->
