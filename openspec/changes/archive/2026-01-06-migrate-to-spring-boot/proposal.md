# Migrate to Spring Boot (Stdio Phase)

## Goal
Refactor the application from a standalone Java CLI to a **Spring Boot** application while **maintaining the existing `stdio` transport**. This is the first step in a phased migration, focusing on establishing the Spring container and Dependency Injection before changing the transport layer.

## Motivation
The user wants to transition to a hybrid architecture (Web UI + MCP) but prefers an incremental approach. This step validates that the Spring configuration, Bean wiring, and build process are correct without introducing the complexity of a new HTTP/SSE transport simultaneously.

## What Changes
1.  **Framework**: Adopt Spring Boot 3.x.
2.  **Architecture**: Refactor `Main.java` into a Spring Boot Application (`GovernanceApplication`).
3.  **Wiring**: Move manual tool instantiation to a `@Configuration` class (`McpConfig`), using Spring Beans for `VaultProvider`, `TimelogService`, and Tools.
4.  **Transport**: Retain `StdioServerTransport` within a `CommandLineRunner` to ensure backward compatibility with existing clients.
5.  **Configuration**: Use `application.properties` for environment variables (e.g., `FPF_VAULT_PATH`).
