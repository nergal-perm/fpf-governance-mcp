package org.fpf.governance.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.services.TimelogService;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class FetchTimelogsTool implements ToolSpecification {
    private final TimelogService timelogService;

    public FetchTimelogsTool(TimelogService timelogService) {
        this.timelogService = timelogService;
    }

    @Override
    public BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult> handler() {
        return (exchange, request) -> {
            int limit = 10; // Default
            if (request.arguments() != null && request.arguments().containsKey("limit")) {
                Object limitObj = request.arguments().get("limit");
                if (limitObj instanceof Number) {
                    limit = ((Number) limitObj).intValue();
                } else if (limitObj instanceof String) {
                    try {
                        limit = Integer.parseInt((String) limitObj);
                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            List<Map<String, Object>> logs = timelogService.fetchTimelogs(limit);

            // Convert logs to JSON string for output
            // For now, we rely on toString() or simple manual formatting if JSON library
            // isn't ubiquitous?
            // Ideally we'd use Jackson, but let's see if we can just pass the raw object if
            // the SDK supports it.
            // The SDK expects TextContent or ImageContent. So we must serialize.
            // We'll use a simple string representation for now, or assume Jackson is
            // available since Mcp uses it.
            // Wait, we saw Jackson usage in Main.java imports (that were missing).

            // Let's use a simple robust toString for now to avoid dependency hell if
            // Jackson isn't configured in this module yet
            // Or better, let's just return it as a string.

            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < logs.size(); i++) {
                sb.append(logs.get(i).toString()); // Improve this if real JSON is needed
                if (i < logs.size() - 1)
                    sb.append(", ");
            }
            sb.append("]");

            return McpSchema.CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent(sb.toString())))
                    .isError(false)
                    .build();
        };
    }

    @Override
    public McpSchema.Tool toolSpecification() {
        return McpSchema.Tool.builder()
                .name("fetch_timelogs")
                .description(
                        "Fetch pending timelogs from the queue. Returns a list of records. 'payload' field is often nested JSON.")
                .inputSchema(new McpSchema.JsonSchema(
                        "object",
                        Map.of("limit", Map.of("type", "integer", "description", "Max records to fetch (default 10)")),
                        List.of(), // No required fields
                        null, null, null))
                .build();
    }
}
