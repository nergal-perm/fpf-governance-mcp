package org.fpf.governance.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;

import java.util.function.BiFunction;

import static io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import static io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import static io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import static io.modelcontextprotocol.spec.McpSchema.Tool;

public interface ToolSpecification {

    default SyncToolSpecification asSpecification() {
        return SyncToolSpecification.builder()
                .tool(this.toolSpecification())
                .callHandler(this.handler())
                .build();
    }

    BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> handler();

    Tool toolSpecification();
}
