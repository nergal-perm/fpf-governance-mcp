# Design: Spring Boot with Stdio Transport

## Architecture

### 1. Spring Boot Integration
*   Add `spring-boot-starter` (and `starter-test`) to `pom.xml`.
*   Create `GovernanceApplication` annotated with `@SpringBootApplication`.

### 2. Dependency Injection (McpConfig)
We will create a configuration class to define our components as Beans:
*   **`VaultProvider`**: Configured via `@Value("${fpf.vault.path}")`.
*   **`DynamoDbClient`**: Conditional bean based on configuration.
*   **`TimelogService`**: Injected with `DynamoDbClient`.
*   **`ToolSpecification` Beans**: `EchoTool`, `ValidateArtifact`, `FetchTimelogsTool`, `DeleteTimelogTool`.

### 3. Execution (StdioRunner)
*   Implement a `CommandLineRunner` bean.
*   In the `run` method, execute the existing `McpServer.sync(StdioServerTransport)` logic.
*   Inject the `List<ToolSpecification>` via the constructor (Spring auto-wires the list of all beans of this type).

## Trade-offs
*   **Blocking**: The `StdioServerTransport` blocks the main thread. This is acceptable (and required) for a CLI tool, but effectively pauses the Spring application lifecycle after the runner starts.
*   **Web Server**: We will explicitly *disable* the web server (`spring.main.web-application-type=none`) for this phase to ensure clean `stdio` output without Tomcat logs polluting the stream.
