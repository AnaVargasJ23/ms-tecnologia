package com.onclass.tecnologia.infrastructure.entrypoints.handler;

import com.onclass.tecnologia.domain.api.ITecnologiaServicePort;
import com.onclass.tecnologia.domain.excepcion.TecnologiaException;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.ErrorResponse;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.TecnologiaRequest;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.TecnologiaResponse;
import com.onclass.tecnologia.infrastructure.entrypoints.mapper.TecnologiaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TecnologiaHandler {

    private final ITecnologiaServicePort tecnologiaServicePort;
    private final TecnologiaMapper tecnologiaMapper;

    public Mono<ServerResponse> registrar(ServerRequest request) {
        return request.bodyToMono(TecnologiaRequest.class)
                .map(tecnologiaMapper::toDomain)
                .flatMap(tecnologiaServicePort::registrar)
                .map(tecnologiaMapper::toResponse)
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response))
                .onErrorResume(TecnologiaException.class, e -> {
                    log.error("Error de negocio: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorResponse.builder()
                                    .code(e.getCode())
                                    .message(e.getMessage())
                                    .build());
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error inesperado: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorResponse.builder()
                                    .code("TEC-500")
                                    .message("Error interno del servidor")
                                    .build());
                });
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .body(tecnologiaServicePort.listarTodas()
                        .map(tecnologiaMapper::toResponse), TecnologiaResponse.class);
    }

    public Mono<ServerResponse> buscarPorId(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return tecnologiaServicePort.buscarPorId(id)
                .map(tecnologiaMapper::toResponse)
                .flatMap(t -> ServerResponse.ok().bodyValue(t))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return tecnologiaServicePort.eliminarPorId(id)
                .then(ServerResponse.noContent().build())
                .onErrorResume(Exception.class, e ->
                        ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }

}