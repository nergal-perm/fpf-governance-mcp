package org.fpf.governance.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import static io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import static io.modelcontextprotocol.spec.McpSchema.JsonSchema;
import static io.modelcontextprotocol.spec.McpSchema.TextContent;
import static io.modelcontextprotocol.spec.McpSchema.Tool;

public class EchoTool implements ToolSpecification {
    @Override
    public BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> handler() {
        return (exchange, request) -> {
            String message = request.arguments().getOrDefault("message", "No message provided").toString();
            return CallToolResult.builder()
                    .content(List.of(new TextContent(message)))
                    .isError(false)
                    .build();
        };
    }

    @Override
    public Tool toolSpecification() {
        return Tool.builder()
                .name("echo")
                .description("Echoes back the input message")
                .inputSchema(new JsonSchema(
                                "object",
                                Map.of("message", Map.of("type", "string")),
                                List.of("message"),
                                null, null, null
                        )
                )
                .build();
    }
}
