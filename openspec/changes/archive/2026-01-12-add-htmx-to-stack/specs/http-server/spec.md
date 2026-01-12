# http-server Specification Delta

## MODIFIED Requirements

### Requirement: HTTP Transport

The application MUST communicate via HTTP Server-Sent Events (SSE) to support standard MCP clients and remote connections, AND serve HTML fragments for the web interface.

#### Scenario: HTMX Request for Fragment

  - **Given** the server is running
  - **When** a client sends a request with an `HX-Request` header
  - **Then** the server MUST respond with a partial HTML fragment instead of a full document.

#### Scenario: Serve HTMX Library

  - **Given** the server is running
  - **When** a client requests `/js/htmx.min.js` (or configured path)
  - **Then** the server MUST return the HTMX library with the correct MIME type.

#### Scenario: On-Demand DB Status Check

  - **Given** the web interface is loaded
  - **And** the user sees a "Check Neo4j Status" button
  - **When** the user clicks the button
  - **Then** the button is replaced by a text element indicating "Connected" or "Disconnected"
  - **And** the application logs are free of automatic startup connection checks.