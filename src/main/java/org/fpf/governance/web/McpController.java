package org.fpf.governance.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class McpController {

    private final SpringMcpTransportProvider transportProvider;
    private final McpJsonMapper mapper;
    private final ObjectMapper objectMapper;

    public McpController(SpringMcpTransportProvider transportProvider, McpJsonMapper mapper, ObjectMapper objectMapper) {
        this.transportProvider = transportProvider;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/mcp/sse")
    public SseEmitter handleSse(HttpServletResponse response) {
        // Set a long timeout for SSE connection
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String sessionId = transportProvider.createSession(emitter);
        
        response.setHeader("Mcp-Session-Id", sessionId);

        // Send the endpoint event pointing to where to POST messages
        try {
            emitter.send(SseEmitter.event()
                    .name("endpoint")
                    .data("/mcp/sse?sessionId=" + sessionId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        
        return emitter;
    }

    @PostMapping("/mcp/sse")
    public Mono<Void> handleMessage(@RequestParam String sessionId, @RequestBody String body, HttpServletResponse response) {
        McpServerSession session = transportProvider.getSession(sessionId);
        if (session == null) {
            return Mono.error(new IllegalArgumentException("Session not found: " + sessionId));
        }

        if (body.contains("\"method\":\"initialize\"") || body.contains("\"method\": \"initialize\"")) {
            response.setHeader("Mcp-Session-Id", sessionId);
        }

        try {
            // Manual polymorphic deserialization
            JsonNode root = objectMapper.readTree(body);
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

            // Handle the message via the session
            return session.handle(message);
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
