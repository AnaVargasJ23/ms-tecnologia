package com.onclass.tecnologia.infrastructure.adapters.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TecnologiaR2dbcRepository
        extends ReactiveCrudRepository<TecnologiaEntity, Long> {

    Mono<Boolean> existsByNombre(String nombre);
}