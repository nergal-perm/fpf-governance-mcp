# mcp-protocol Specification

## ADDED Requirements

### Requirement: Tool Support

The server MUST support the listing and execution of tools.

#### Scenario: Advertise Tools Capability
-   **Given** the server is initializing
-   **When** it sends the `initialize` result
-   **Then** the `capabilities` object MUST contain a defined `tools` key.

#### Scenario: List Tools
-   **Given** the server is running
-   **When** a "tools/list" request is received
-   **Then** the server returns a list of available tools
-   **And** the list MUST include at least one tool named "echo".

#### Scenario: Call Hub Tool
-   **Given** the server provides an "echo" tool
-   **When** a "tools/call" request is sent for "echo" with arguments `{"message": "hello"}`
-   **Then** the server returns a text result containing "hello".
