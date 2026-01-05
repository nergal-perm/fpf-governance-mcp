package org.fpf.governance.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TimelogServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    private TimelogService timelogService;
    private final String pk = "id";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String tableName = "test-table";
        timelogService = new TimelogService(dynamoDbClient, tableName, pk);
    }

    @Test
    void fetchTimelogs_shouldReturnMappedResults() {
        // Arrange
        Map<String, AttributeValue> item = Map.of(
                "id", AttributeValue.builder().s("123").build(),
                "payload", AttributeValue.builder().s("{\"task\":\"test\"}").build());
        ScanResponse response = ScanResponse.builder()
                .items(List.of(item))
                .build();
        when(dynamoDbClient.scan(any(ScanRequest.class))).thenReturn(response);

        // Act
        List<Map<String, Object>> result = timelogService.fetchTimelogs(10);

        // Assert
        assertEquals(1, result.size());
        assertEquals("123", result.getFirst().get("id"));
        assertEquals("{\"task\":\"test\"}", result.getFirst().get("payload"));
        verify(dynamoDbClient).scan(any(ScanRequest.class));
    }

    @Test
    void deleteTimelog_shouldCallDeleteItem() {
        // Act
        timelogService.deleteTimelog("123");

        // Assert
        verify(dynamoDbClient).deleteItem(any(DeleteItemRequest.class));
    }
}
