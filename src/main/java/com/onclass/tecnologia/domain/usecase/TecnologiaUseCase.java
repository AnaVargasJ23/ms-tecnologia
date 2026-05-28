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
        try {
            validar(tecnologia);
        } catch (TecnologiaException e) {
            return Mono.error(e);
        }
        return persistencePort.existePorNombre(tecnologia.getNombre())
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

    private void validar(Tecnologia tecnologia) {
        if (tecnologia.getNombre() == null || tecnologia.getNombre().isBlank()) {
            throw new TecnologiaException(
                    TecnologiaErrorEnum.NOMBRE_OBLIGATORIO.getCode(),
                    TecnologiaErrorEnum.NOMBRE_OBLIGATORIO.getMessage());
        }
        if (tecnologia.getNombre().length() > 50) {
            throw new TecnologiaException(
                    TecnologiaErrorEnum.NOMBRE_MAX_50.getCode(),
                    TecnologiaErrorEnum.NOMBRE_MAX_50.getMessage());
        }
        if (tecnologia.getDescripcion() == null || tecnologia.getDescripcion().isBlank()) {
            throw new TecnologiaException(
                    TecnologiaErrorEnum.DESCRIPCION_OBLIGATORIA.getCode(),
                    TecnologiaErrorEnum.DESCRIPCION_OBLIGATORIA.getMessage());
        }
        if (tecnologia.getDescripcion().length() > 90) {
            throw new TecnologiaException(
                    TecnologiaErrorEnum.DESCRIPCION_MAX_90.getCode(),
                    TecnologiaErrorEnum.DESCRIPCION_MAX_90.getMessage());
        }
    }
}