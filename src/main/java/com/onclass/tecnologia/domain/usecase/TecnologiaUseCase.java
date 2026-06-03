package com.onclass.tecnologia.domain.usecase;

import com.onclass.tecnologia.domain.api.ITecnologiaServicePort;
import com.onclass.tecnologia.domain.enums.TecnologiaErrorEnum;
import com.onclass.tecnologia.domain.excepcion.TecnologiaException;
import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TecnologiaUseCase implements ITecnologiaServicePort {

    private final ITecnologiaPersistencePort persistencePort;

    @Override
    public Mono<Tecnologia> registrar(Tecnologia tecnologia) {
        return validar(tecnologia)
                .flatMap(t -> persistencePort.existePorNombre(t.getNombre()))
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new TecnologiaException(
                                TecnologiaErrorEnum.NOMBRE_DUPLICADO.getCode(),
                                TecnologiaErrorEnum.NOMBRE_DUPLICADO.getMessage()));
                    }
                    return persistencePort.guardar(tecnologia);
                });
    }

    @Override
    public Flux<Tecnologia> listarTodas() {
        return persistencePort.listarTodas();
    }

    @Override
    public Mono<Tecnologia> buscarPorId(Long id) {
        return persistencePort.buscarPorId(id);
    }

    @Override
    public Mono<Void> eliminarPorId(Long id) {
        return persistencePort.eliminarPorId(id);
    }

    private Mono<Tecnologia> validar(Tecnologia tecnologia) {
        if (tecnologia.getNombre() == null || tecnologia.getNombre().isBlank()) {
            return Mono.error(new TecnologiaException(
                    TecnologiaErrorEnum.NOMBRE_OBLIGATORIO.getCode(),
                    TecnologiaErrorEnum.NOMBRE_OBLIGATORIO.getMessage()));
        }
        if (tecnologia.getNombre().length() > 50) {
            return Mono.error(new TecnologiaException(
                    TecnologiaErrorEnum.NOMBRE_MAX_50.getCode(),
                    TecnologiaErrorEnum.NOMBRE_MAX_50.getMessage()));
        }
        if (tecnologia.getDescripcion() == null || tecnologia.getDescripcion().isBlank()) {
            return Mono.error(new TecnologiaException(
                    TecnologiaErrorEnum.DESCRIPCION_OBLIGATORIA.getCode(),
                    TecnologiaErrorEnum.DESCRIPCION_OBLIGATORIA.getMessage()));
        }
        if (tecnologia.getDescripcion().length() > 90) {
            return Mono.error(new TecnologiaException(
                    TecnologiaErrorEnum.DESCRIPCION_MAX_90.getCode(),
                    TecnologiaErrorEnum.DESCRIPCION_MAX_90.getMessage()));
        }
        return Mono.just(tecnologia);
    }
}
