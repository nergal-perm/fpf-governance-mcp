# http-server Specification

## Purpose
Defines the architectural requirements for the MCP Server runtime, which is implemented as a Spring Boot application supporting HTTP/SSE transport and HTMX-based frontend interactions.
## Requirements
### Requirement: Spring Boot Container
The application MUST run within a Spring Boot container to leverage Dependency Injection and standardized configuration.

#### Scenario: Application Startup
- **Given** the application JAR is executed
- **When** the process starts
- **Then** the Spring Context is initialized
- **And** all defined beans (Tools, Services) are instantiated

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

### Requirement: HTMX Support
The application MUST support serving HTML fragments for HTMX-based frontend interactions.

#### Scenario: Serve Static Assets
- **Given** the server is running
- **When** a client requests HTMX library (e.g. via WebJar path)
- **Then** the server serves the JS file

#### Scenario: Serve Partial HTML
- **Given** a request initiated by HTMX (e.g., with `HX-Request` header)
- **When** the endpoint processes the request
- **Then** it returns an HTML fragment (not a full page)
- **And** the content type is `text/html`

### Requirement: Manual Brain Test UI
The web interface SHALL provide a simple tool for the user to manually verify connectivity to the AI Brain.

#### Scenario: User submits a prompt
- **WHEN** the user enters text into the test prompt area and clicks "Send"
- **THEN** the system sends the prompt to the configured Brain
- **AND** displays the generated response text in the interface without a full page reload

