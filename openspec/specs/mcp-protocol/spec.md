# mcp-protocol Specification

## Purpose
The MCP Protocol specification defines how the FPF Governance server implements the Model Context Protocol. It covers the server's capabilities, tool exposure mechanism, and protocol compliance (JSON-RPC 2.0) needed to interoperate with MCP clients (like Claude Desktop or IDE extensions).
## Requirements
### Requirement: Initialize Handshake

The server MUST parse the `initialize` JSON-RPC request using a type-safe model and respond with a valid structured result.

#### Scenario: Client sends valid initialize request
-   **Given** the server is running
-   **When** a valid JSON-RPC "initialize" request is received
-   **Then** the server deserializes it to a `JsonRpcRequest` object
-   **And** the server serializes a `JsonRpcResponse` with `result` containing server capabilities
-   **And** the `id` of the response matches the request `id`

### Requirement: JSON-RPC Error Handling

The server MUST handle invalid JSON and invalid RPC requests according to the JSON-RPC 2.0 specification.

#### Scenario: Client sends malformed JSON
-   **Given** the server is running
-   **When** a malformed JSON string (e.g. `{"jsonrpc":`) is sent
-   **Then** the server responds with a JSON-RPC Parse Error (code -32700)

#### Scenario: Client sends invalid RPC object
-   **Given** the server is running
-   **When** a valid JSON object is sent that is not a valid RPC request (e.g. missing `method`)
-   **Then** the server responds with a JSON-RPC Invalid Request Error (code -32600)

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

