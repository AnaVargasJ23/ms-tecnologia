package com.onclass.tecnologia.domain.usecase;

import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TecnologiaUseCase {

    private final ITecnologiaPersistencePort persistencePort;

    public Mono<Tecnologia> registrar(Tecnologia tecnologia) {
        return Mono.fromCallable(() -> {
                    validar(tecnologia);
                    return tecnologia;
                })
                .flatMap(t -> persistencePort.existePorNombre(t.getNombre()))
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new IllegalStateException("Ya existe una tecnología con ese nombre"));
                    }
                    return persistencePort.guardar(tecnologia);
                });
    }

    public Flux<Tecnologia> listarTodas() {
        return persistencePort.listarTodas();
    }

    private void validar(Tecnologia tecnologia) {
        if (tecnologia.getNombre() == null || tecnologia.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (tecnologia.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar 50 caracteres");
        }
        if (tecnologia.getDescripcion() == null || tecnologia.getDescripcion().isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (tecnologia.getDescripcion().length() > 90) {
            throw new IllegalArgumentException("La descripción no puede superar 90 caracteres");
        }
    }
}