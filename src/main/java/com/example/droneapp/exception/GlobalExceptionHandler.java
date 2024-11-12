package com.example.droneapp.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status;

        if (ex instanceof IllegalStateException) {
            status = HttpStatus.BAD_REQUEST;
            body.put("error", "Illegal State");
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            body.put("error", "Invalid Argument");
        } else if (ex instanceof ResponseStatusException) {
            status = (HttpStatus) ((ResponseStatusException) ex).getStatusCode();
            body.put("error", ((ResponseStatusException) ex).getReason());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body.put("error", "Internal Server Error");
        }

        body.put("message", ex.getMessage());
        body.put("status", status.value());

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(body);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonBytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
