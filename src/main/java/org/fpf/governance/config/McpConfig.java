package org.fpf.governance.config;

import org.fpf.governance.services.TimelogService;
import org.fpf.governance.tools.DeleteTimelogTool;
import org.fpf.governance.tools.EchoTool;
import org.fpf.governance.tools.FetchTimelogsTool;
import org.fpf.governance.tools.ToolSpecification;
import org.fpf.governance.tools.ValidateArtifact;
import org.fpf.governance.vault.FileVaultProvider;
import org.fpf.governance.vault.VaultProvider;
import org.fpf.governance.web.SpringMcpTransportProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.server.McpServerFeatures;
import reactor.core.publisher.Mono;
import java.util.List;

@Configuration
public class McpConfig {

    @Value("${fpf.vault.path:.}")
    private String vaultPath;

    @Value("${fpf.timelog.table:#{null}}")
    private String timelogTable;

    @Value("${fpf.timelog.pk:id}")
    private String timelogPk;

    @Bean
    public VaultProvider vaultProvider() {
        return new FileVaultProvider(vaultPath);
    }

    @Bean
    public EchoTool echoTool() {
        return new EchoTool();
    }

    @Bean
    public ValidateArtifact validateArtifact(VaultProvider vaultProvider) {
        return new ValidateArtifact(vaultProvider);
    }

    @Bean
    @ConditionalOnProperty(name = "fpf.timelog.table")
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.create();
    }

    @Bean
    @ConditionalOnProperty(name = "fpf.timelog.table")
    public TimelogService timelogService(DynamoDbClient dynamoDbClient) {
        return new TimelogService(dynamoDbClient, timelogTable, timelogPk);
    }

    @Bean
    @ConditionalOnProperty(name = "fpf.timelog.table")
    public FetchTimelogsTool fetchTimelogsTool(TimelogService timelogService) {
        return new FetchTimelogsTool(timelogService);
    }

    @Bean
    @ConditionalOnProperty(name = "fpf.timelog.table")
    public DeleteTimelogTool deleteTimelogTool(TimelogService timelogService) {
        return new DeleteTimelogTool(timelogService);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public McpJsonMapper mcpJsonMapper(ObjectMapper objectMapper) {
        return new JacksonMcpJsonMapper(objectMapper);
    }

    @Bean
    public McpAsyncServer mcpAsyncServer(SpringMcpTransportProvider transportProvider, List<ToolSpecification> tools) {
        return McpServer.async(transportProvider)
                .serverInfo("fpf-governance", "0.1.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .build())
                .tools(tools.stream().map(ts -> McpServerFeatures.AsyncToolSpecification.builder()
                        .tool(ts.toolSpecification())
                        .callHandler((exchange, request) -> Mono.fromCallable(() -> 
                                ts.handler().apply(null, request)
                        ))
                        .build()).toList())
                .build();
    }
}