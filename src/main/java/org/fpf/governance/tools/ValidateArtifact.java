package org.fpf.governance.tools;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.checks.CheckRegistry;
import org.fpf.governance.checks.ValidationResult;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ValidateArtifact implements ToolSpecification {

    @Override
    public BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult> handler() {
        return (exchange, request) -> {
            String path = (String) request.arguments().get("path");
            String content = (String) request.arguments().get("content");

            if (path == null) {
                throw new IllegalArgumentException("Missing path");
            }
            if (content == null) {
                throw new IllegalArgumentException("Missing content");
            }

            ValidationResult result = CheckRegistry.validate(path, content);

            String message = result.valid() ? "Valid" : "Invalid: " + String.join("; ", result.errors());

            return McpSchema.CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent(message)))
                    .isError(!result.valid())
                    .build();
        };
    }

    @Override
    public McpSchema.Tool toolSpecification() {
        return McpSchema.Tool.builder()
                .name("validate_governance_compliance")
                .description(
                        "FPF Governance Linter. REQUIRED: Call this tool to validate content and paths for any new or modified artifact (Hypothesis, "
                        + "Decision, etc.) BEFORE writing to disk to ensure compliance.")
                .inputSchema(new McpSchema.JsonSchema(
                        "object",
                        Map.of(
                                "path", Map.of("type", "string"),
                                "content", Map.of("type", "string")
                        ),
                        List.of("path", "content"),
                        null, null, null
                ))
                .build();
    }
}
