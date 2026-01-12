package org.fpf.governance.web;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpServerSession;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpringMcpTransportProvider implements McpServerTransportProvider {

    private final McpJsonMapper mapper;
    private McpServerSession.Factory sessionFactory;
    private final Map<String, McpServerSession> sessions = new ConcurrentHashMap<>();

    public SpringMcpTransportProvider(McpJsonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void setSessionFactory(McpServerSession.Factory factory) {
        this.sessionFactory = factory;
    }

    @Override
    public reactor.core.publisher.Mono<Void> notifyClients(String method, Object params) {
        return reactor.core.publisher.Flux.fromIterable(sessions.values())
                .flatMap(session -> session.sendNotification(method, params))
                .then();
    }

    @Override
    public reactor.core.publisher.Mono<Void> closeGracefully() {
        return reactor.core.publisher.Flux.fromIterable(sessions.values())
                .flatMap(McpServerSession::closeGracefully)
                .then();
    }

    public String createSession(SseEmitter emitter) {
        if (sessionFactory == null) {
            throw new IllegalStateException("McpServer not initialized (sessionFactory is null)");
        }

        SseMcpTransport transport = new SseMcpTransport(emitter, mapper);
        McpServerSession session = sessionFactory.create(transport);
        
        String sessionId = session.getId();
        // If ID is null (unlikely), generate one.
        if (sessionId == null) {
             sessionId = UUID.randomUUID().toString();
        }
        
        sessions.put(sessionId, session);

        final String finalSessionId = sessionId;
        emitter.onCompletion(() -> sessions.remove(finalSessionId));
        emitter.onTimeout(() -> sessions.remove(finalSessionId));

        return sessionId;
    }

    public McpServerSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
