# mcp-protocol Specification

## Purpose
TBD - created by archiving change stub-mcp-initialize. Update Purpose after archive.
## Requirements
### Requirement: Initialize Handshake

The server MUST respond to the `initialize` JSON-RPC method with a valid result.

#### Scenario: Client sends initialize request
-   **Given** the server is running
-   **When** a JSON-RPC message with method "initialize" is sent to Stdin
-   **Then** the server prints a JSON-RPC success response to Stdout
-   **And** the response includes the server name and version

