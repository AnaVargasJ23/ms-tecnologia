package com.onclass.tecnologia.domain.usecase;

import com.onclass.tecnologia.domain.excepcion.TecnologiaException;
import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TecnologiaUseCaseTest {

    @Mock
    private ITecnologiaPersistencePort persistencePort;

    @InjectMocks
    private TecnologiaUseCase useCase;

    @Test
    void registrar_exitoso() {
        Tecnologia tecnologia = new Tecnologia(null, "Java", "Lenguaje de programación");
        when(persistencePort.existePorNombre("Java")).thenReturn(Mono.just(false));
        when(persistencePort.guardar(any())).thenReturn(Mono.just(new Tecnologia(1L, "Java", "Lenguaje de programación")));

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectNextMatches(t -> t.getId() == 1L && t.getNombre().equals("Java"))
                .verifyComplete();
    }

    @Test
    void registrar_nombreDuplicado_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "Java", "Lenguaje de programación");
        when(persistencePort.existePorNombre("Java")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_nombreVacio_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "", "Descripción válida");

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_nombreNull_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, null, "Descripción válida");

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_nombreMayorA50Chars_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "A".repeat(51), "Descripción válida");

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_descripcionVacia_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "Java", "");

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_descripcionNull_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "Java", null);

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void registrar_descripcionMayorA90Chars_lanzaError() {
        Tecnologia tecnologia = new Tecnologia(null, "Java", "A".repeat(91));

        StepVerifier.create(useCase.registrar(tecnologia))
                .expectError(TecnologiaException.class)
                .verify();
    }

    @Test
    void listarTodas_retornaLista() {
        when(persistencePort.listarTodas()).thenReturn(Flux.just(
                new Tecnologia(1L, "Java", "Lenguaje de programación"),
                new Tecnologia(2L, "Python", "Lenguaje de scripting")
        ));

        StepVerifier.create(useCase.listarTodas())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void buscarPorId_exitoso() {
        when(persistencePort.buscarPorId(1L))
                .thenReturn(Mono.just(new Tecnologia(1L, "Java", "Lenguaje de programación")));

        StepVerifier.create(useCase.buscarPorId(1L))
                .expectNextMatches(t -> t.getId() == 1L && t.getNombre().equals("Java"))
                .verifyComplete();
    }

    @Test
    void buscarPorId_noExiste_retornaVacio() {
        when(persistencePort.buscarPorId(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.buscarPorId(999L))
                .verifyComplete();
    }

    @Test
    void eliminarPorId_exitoso() {
        when(persistencePort.eliminarPorId(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.eliminarPorId(1L))
                .verifyComplete();
    }

}