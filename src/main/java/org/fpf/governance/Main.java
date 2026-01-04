package org.fpf.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.tools.EchoTool;
import org.fpf.governance.tools.ValidateArtifact;

public class Main {

    public static void main(String[] args) {
        McpServer.sync(new StdioServerTransportProvider(new JacksonMcpJsonMapper(new ObjectMapper())))
                .serverInfo("fpf-governance", "0.1.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(false)
                        .build())
                .tools(
                        new EchoTool().asSpecification(),
                        new ValidateArtifact().asSpecification()
                )
                .build();
    }
}
