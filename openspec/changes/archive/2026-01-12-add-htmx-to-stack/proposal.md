# Add HTMX to Tech Stack Proposal

## Summary
Incorporate HTMX into the project's technology stack for the web application's frontend.

## Goal
To enable a lightweight, dynamic user interface for the "Board Room" (Web App) by using HTMX to perform partial page updates via HTML fragments returned from the server.

## Scope
- Update `openspec/project.md` to include HTMX in the Tech Stack.
- Update `http-server` specification to include requirements for serving HTML fragments compatible with HTMX.

## Risks
- **Complexity**: Ensuring the server correctly handles both JSON-RPC (MCP) and HTML fragment requests.
- **State Management**: Managing frontend state when using partial updates.
