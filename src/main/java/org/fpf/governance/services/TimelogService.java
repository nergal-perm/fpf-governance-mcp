package org.fpf.governance.services;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimelogService {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName;
    private final String partitionKey;

    // Parsing logic could be complex if payload is stringified JSON.
    // We'll expose raw structure first as per design.

    public TimelogService(DynamoDbClient dynamoDbClient, String tableName, String partitionKey) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
        this.partitionKey = partitionKey;
    }

    public List<Map<String, Object>> fetchTimelogs(int limit) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .limit(limit)
                .build();

        ScanResponse response = dynamoDbClient.scan(scanRequest);

        return response.items().stream()
                .map(this::convertAttributeMap)
                .collect(Collectors.toList());
    }

    public void deleteTimelog(String id) {
        DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(partitionKey, AttributeValue.builder().s(id).build()))
                .build();

        dynamoDbClient.deleteItem(deleteRequest);
    }

    private Map<String, Object> convertAttributeMap(Map<String, AttributeValue> item) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            result.put(entry.getKey(), convertAttributeValue(entry.getValue()));
        }
        return result;
    }

    private Object convertAttributeValue(AttributeValue value) {
        if (value.s() != null)
            return value.s();
        if (value.n() != null)
            return value.n(); // Return as String or parse? Keeping simpler for now.
        if (value.bool() != null)
            return value.bool();
        if (value.hasM())
            return convertAttributeMap(value.m());
        if (value.hasL())
            return value.l().stream().map(this::convertAttributeValue).collect(Collectors.toList());
        if (value.nul())
            return null;
        return value.toString(); // Fallback
    }
}
