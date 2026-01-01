## MODIFIED Requirements
### Requirement: Initialize Handshake

The server MUST parse the `initialize` JSON-RPC request using a type-safe model and respond with a valid structured result.

#### Scenario: Client sends valid initialize request
-   **Given** the server is running
-   **When** a valid JSON-RPC "initialize" request is received
-   **Then** the server deserializes it to a `JsonRpcRequest` object
-   **And** the server serializes a `JsonRpcResponse` with `result` containing server capabilities
-   **And** the `id` of the response matches the request `id`

## ADDED Requirements
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
