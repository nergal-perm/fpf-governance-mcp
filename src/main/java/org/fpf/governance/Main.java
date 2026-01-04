package org.fpf.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.fpf.governance.tools.EchoTool;
import org.fpf.governance.tools.ValidateArtifact;

import org.fpf.governance.vault.FileVaultProvider;
import org.fpf.governance.vault.VaultProvider;

public class Main {

        public static void main(String[] args) {
                String vaultPath = System.getenv("FPF_VAULT_PATH");
                if (args.length > 0) {
                        for (String arg : args) {
                                if (arg.startsWith("--vault-path=")) {
                                        vaultPath = arg.substring("--vault-path=".length());
                                }
                        }
                }

                if (vaultPath == null) {
                        System.err.println(
                                        "Warning: --vault-path not specified and FPF_VAULT_PATH not set. Using current directory.");
                        vaultPath = ".";
                }

                VaultProvider vaultProvider = new FileVaultProvider(vaultPath);

                McpServer.sync(new StdioServerTransportProvider(new JacksonMcpJsonMapper(new ObjectMapper())))
                                .serverInfo("fpf-governance", "0.1.0")
                                .capabilities(McpSchema.ServerCapabilities.builder()
                                                .tools(false)
                                                .build())
                                .tools(
                                                new EchoTool().asSpecification(),
                                                new ValidateArtifact(vaultProvider).asSpecification())
                                .build();
        }
}
