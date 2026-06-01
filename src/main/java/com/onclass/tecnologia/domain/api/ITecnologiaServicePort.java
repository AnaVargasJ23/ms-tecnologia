package com.onclass.tecnologia.domain.api;

import com.onclass.tecnologia.domain.model.Tecnologia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITecnologiaServicePort {
    Mono<Tecnologia> registrar(Tecnologia tecnologia);
    Flux<Tecnologia> listarTodas();
    Mono<Tecnologia> buscarPorId(Long id);
    Mono<Void> eliminarPorId(Long id);


}
