package org.fpf.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.tools.ToolSpecification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StdioRunner implements CommandLineRunner {

    private final List<ToolSpecification> tools;

    public StdioRunner(List<ToolSpecification> tools) {
        this.tools = tools;
    }

    @Override
    public void run(String... args) throws Exception {
        McpServer.sync(new StdioServerTransportProvider(new JacksonMcpJsonMapper(new ObjectMapper())))
                .serverInfo("fpf-governance", "0.1.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(false)
                        .build())
                .tools(tools.stream().map(ToolSpecification::asSpecification).toList())
                .build();
    }
}
