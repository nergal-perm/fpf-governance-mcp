# Design: Modern MCP SSE Endpoint

## Context
The current implementation uses a legacy split-endpoint approach for the Model Context Protocol (MCP):
- `GET /sse` for the EventSource connection.
- `POST /messages` for sending JSON-RPC messages.

The modern convention and user request favor a single endpoint `mcp/sse` that handles both the SSE handshake and message posting (via POST).

## Proposed Changes

### 1. Update `McpController`
- Change `handleSse` mapping from `/sse` to `/mcp/sse`.
- Change `handleMessage` mapping from `/messages` to `/mcp/sse`.
- Make the `/mcp/sse` endpoint return SSE by setting a header `Content-Type: text/event-stream` for the response.
- Update the response for the handshake message (`initialize`) to include the
  generic `MCP-Session-ID` header.

### 2. Backward Compatibility
- This change breaks clients expecting the old `/sse` endpoint. As this is an internal tool/early development phase, we assume this is acceptable.

## Impact
- **Controllers**: `McpController.java` will be modified.
- **Config**: No major config changes expected.
