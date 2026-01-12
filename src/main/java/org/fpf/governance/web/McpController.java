package org.fpf.governance.web;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class McpController {

    private final SpringMcpTransportProvider transportProvider;
    private final McpJsonMapper mapper;

    public McpController(SpringMcpTransportProvider transportProvider, McpJsonMapper mapper) {
        this.transportProvider = transportProvider;
        this.mapper = mapper;
    }

    @GetMapping("/sse")
    public SseEmitter handleSse() {
        // Set a long timeout for SSE connection
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String sessionId = transportProvider.createSession(emitter);
        
        // Send the endpoint event pointing to where to POST messages
        try {
            emitter.send(SseEmitter.event()
                    .name("endpoint")
                    .data("/messages?sessionId=" + sessionId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        
        return emitter;
    }

    @PostMapping("/messages")
    public Mono<Void> handleMessage(@RequestParam String sessionId, @RequestBody String body) {
        McpServerSession session = transportProvider.getSession(sessionId);
        if (session == null) {
            return Mono.error(new IllegalArgumentException("Session not found: " + sessionId));
        }

        try {
            // Deserialize the message
            McpSchema.JSONRPCMessage message = mapper.readValue(body, McpSchema.JSONRPCMessage.class);
            // Handle the message via the session
            return session.handle(message);
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
