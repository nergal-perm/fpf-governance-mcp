package org.fpf.governance.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.services.TimelogService;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class DeleteTimelogTool implements ToolSpecification {
    private final TimelogService timelogService;

    public DeleteTimelogTool(TimelogService timelogService) {
        this.timelogService = timelogService;
    }

    @Override
    public BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult> handler() {
        return (exchange, request) -> {
            String id = (String) request.arguments().get("id");
            if (id == null) {
                throw new IllegalArgumentException("Missing 'id' argument");
            }

            timelogService.deleteTimelog(id);

            return McpSchema.CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent("Successfully deleted timelog: " + id)))
                    .isError(false)
                    .build();
        };
    }

    @Override
    public McpSchema.Tool toolSpecification() {
        return McpSchema.Tool.builder()
                .name("delete_timelog")
                .description(
                        "Delete a processed timelog by ID. Use this after successfully saving the information to the Vault.")
                .inputSchema(new McpSchema.JsonSchema(
                        "object",
                        Map.of("id", Map.of("type", "string", "description", "The ID of the record to delete")),
                        List.of("id"),
                        null, null, null))
                .build();
    }
}
