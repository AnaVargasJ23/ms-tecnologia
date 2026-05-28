package com.onclass.tecnologia.application.handler;

import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.domain.usecase.TecnologiaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TecnologiaHandler {

    private final TecnologiaUseCase tecnologiaUseCase;

    public Mono<ServerResponse> registrar(ServerRequest request) {
        return request.bodyToMono(Tecnologia.class)
                .flatMap(tecnologiaUseCase::registrar)
                .flatMap(saved -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(saved))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getMessage()))
                .onErrorResume(IllegalStateException.class, e ->
                        ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .body(tecnologiaUseCase.listarTodas(), Tecnologia.class);
    }
}