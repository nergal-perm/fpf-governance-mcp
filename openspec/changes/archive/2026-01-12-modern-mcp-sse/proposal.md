# Proposal: Modern MCP SSE Endpoint

Switch the MCP transport implementation to use a single `/mcp/sse` endpoint for both establishing the SSE connection and handling JSON-RPC messages, replacing the legacy `/sse` and `/messages` split.
