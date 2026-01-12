package org.fpf.governance.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class McpJsonDeserializationTest {

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\"method\":\"initialize\",\"params\":{\"protocolVersion\":\"2025-11-25\",\"capabilities\":{\"roots\":{\"listChanged\":true}},\"clientInfo\":{\"name\":\"gemini-cli-mcp-client\",\"version\":\"0.0.1\"}},\"jsonrpc\":\"2.0\",\"id\":1}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(json);
        McpSchema.JSONRPCMessage message;
        
        if (root.has("method")) {
            if (root.has("id")) {
                message = objectMapper.treeToValue(root, McpSchema.JSONRPCRequest.class);
            } else {
                message = objectMapper.treeToValue(root, McpSchema.JSONRPCNotification.class);
            }
        } else if (root.has("result") || root.has("error")) {
            message = objectMapper.treeToValue(root, McpSchema.JSONRPCResponse.class);
        } else {
            throw new IllegalArgumentException("Unknown JSON-RPC message type");
        }

        assertNotNull(message);
        assertTrue(message instanceof McpSchema.JSONRPCRequest);
    }
}
