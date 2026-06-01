package com.onclass.tecnologia.domain.spi;

import com.onclass.tecnologia.domain.model.Tecnologia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITecnologiaPersistencePort {

    //mono cuando retorna 0 o 1 elemento, se espera un solo resultado
    //flux retorna 0 a N elementos, se espera una lista

    Mono<Tecnologia> guardar(Tecnologia tecnologia);
    Mono<Boolean> existePorNombre(String nombre);
    Flux<Tecnologia> listarTodas();
    Mono<Tecnologia> buscarPorId(Long id);
    Mono<Void> eliminarPorId(Long id);
}

