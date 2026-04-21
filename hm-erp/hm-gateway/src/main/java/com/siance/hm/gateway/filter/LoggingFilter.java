package com.siance.hm.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String correlationId = request.getHeaders().getFirst(CORRELATION_ID);
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        log.info("Incoming request: {} {} | correlationId={} | remoteAddr={}",
                request.getMethod(), request.getURI().getPath(),
                correlationId, request.getRemoteAddress());

        final String finalCorrelationId = correlationId;
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CORRELATION_ID, finalCorrelationId)
                .build();

        long startTime = System.currentTimeMillis();
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("Response: {} {} | status={} | duration={}ms | correlationId={}",
                            request.getMethod(), request.getURI().getPath(),
                            exchange.getResponse().getStatusCode(), duration, finalCorrelationId);
                }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
