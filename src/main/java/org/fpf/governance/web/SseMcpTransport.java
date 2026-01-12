package org.fpf.governance.web;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.TypeRef;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class SseMcpTransport implements McpServerTransport {
    private static final Logger log = LoggerFactory.getLogger(SseMcpTransport.class);
    
    private final SseEmitter emitter;
    private final McpJsonMapper mapper;

    public SseMcpTransport(SseEmitter emitter, McpJsonMapper mapper) {
        this.emitter = emitter;
        this.mapper = mapper;
    }

    @Override
    public Mono<Void> sendMessage(McpSchema.JSONRPCMessage message) {
        return Mono.fromRunnable(() -> {
            try {
                String json = mapper.writeValueAsString(message);
                emitter.send(json);
            } catch (IOException e) {
                log.error("Failed to send message", e);
                emitter.completeWithError(e);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void close() {
        emitter.complete();
    }

    @Override
    public Mono<Void> closeGracefully() {
        return Mono.fromRunnable(this::close);
    }

    @Override
    public <T> T unmarshalFrom(Object o, TypeRef<T> typeRef) {
        return mapper.convertValue(o, typeRef);
    }
}
