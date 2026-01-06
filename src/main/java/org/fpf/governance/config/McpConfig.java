package org.fpf.governance.config;

import org.fpf.governance.services.TimelogService;
import org.fpf.governance.tools.DeleteTimelogTool;
import org.fpf.governance.tools.EchoTool;
import org.fpf.governance.tools.FetchTimelogsTool;
import org.fpf.governance.tools.ValidateArtifact;
import org.fpf.governance.vault.FileVaultProvider;
import org.fpf.governance.vault.VaultProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

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
}
