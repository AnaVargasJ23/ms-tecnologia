package com.onclass.tecnologia.infrastructure.adapters.persistence;

import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TecnologiaPersistenceAdapter implements ITecnologiaPersistencePort {

    private final TecnologiaR2dbcRepository repository;

    @Override
    public Mono<Tecnologia> guardar(Tecnologia tecnologia) {
        return repository.save(toEntity(tecnologia))
                .map(this::toDomain);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Flux<Tecnologia> listarTodas() {
        return repository.findAll()
                .map(this::toDomain);
    }

    private TecnologiaEntity toEntity(Tecnologia tecnologia) {
        return new TecnologiaEntity(
                tecnologia.getId(),
                tecnologia.getNombre(),
                tecnologia.getDescripcion()
        );
    }

    private Tecnologia toDomain(TecnologiaEntity entity) {
        return new Tecnologia(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion()
        );
    }

    @Override
    public Mono<Tecnologia> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Mono<Void> eliminarPorId(Long id) {
        return repository.deleteById(id);
    }

}