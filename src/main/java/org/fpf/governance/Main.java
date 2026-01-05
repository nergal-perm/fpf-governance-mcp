package org.fpf.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.services.TimelogService;
import org.fpf.governance.tools.DeleteTimelogTool;
import org.fpf.governance.tools.EchoTool;
import org.fpf.governance.tools.FetchTimelogsTool;
import org.fpf.governance.tools.ToolSpecification;
import org.fpf.governance.tools.ValidateArtifact;
import org.fpf.governance.vault.FileVaultProvider;
import org.fpf.governance.vault.VaultProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String vaultPath = System.getenv("FPF_VAULT_PATH");
        for (String arg : args) {
            if (arg.startsWith("--vault-path=")) {
                vaultPath = arg.substring("--vault-path=".length());
            }
        }

        if (vaultPath == null) {
            System.err.println(
                    "Warning: --vault-path not specified and FPF_VAULT_PATH not set. Using current directory.");
            vaultPath = ".";
        }

        VaultProvider vaultProvider = new FileVaultProvider(vaultPath);

        String timelogTable = System.getenv("FPF_TIMELOG_TABLE");
        String timelogPk = System.getenv("FPF_TIMELOG_PK");
        if (timelogPk == null) {
            timelogPk = "id";
        }

        List<ToolSpecification> tools = new ArrayList<>();
        tools.add(new EchoTool());
        tools.add(new ValidateArtifact(vaultProvider));

        if (timelogTable != null) {
            try {
                DynamoDbClient dynamoDbClient = DynamoDbClient.create();
                TimelogService timelogService = new TimelogService(
                        dynamoDbClient, timelogTable,
                        timelogPk
                );
                tools.add(new FetchTimelogsTool(timelogService));
                tools.add(new DeleteTimelogTool(timelogService));
            } catch (Exception e) {
                System.err.println("Warning: Failed to initialize DynamoDB tools: " + e.getMessage());
            }
        } else {
            System.err.println("Warning: FPF_TIMELOG_TABLE not set. Timelog tools will be disabled.");
        }

        McpServer.sync(new StdioServerTransportProvider(new JacksonMcpJsonMapper(new ObjectMapper())))
                .serverInfo("fpf-governance", "0.1.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(false)
                        .build())
                .tools(tools.stream().map(ToolSpecification::asSpecification).toList())
                .build();
    }
}
