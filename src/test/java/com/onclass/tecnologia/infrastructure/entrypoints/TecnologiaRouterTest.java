package com.onclass.tecnologia.infrastructure.entrypoints;

import com.onclass.tecnologia.domain.api.ITecnologiaServicePort;
import com.onclass.tecnologia.domain.excepcion.TecnologiaException;
import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.infrastructure.configuration.BeanConfiguration;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.TecnologiaRequest;
import com.onclass.tecnologia.infrastructure.entrypoints.handler.TecnologiaHandler;
import com.onclass.tecnologia.infrastructure.entrypoints.mapper.TecnologiaMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({TecnologiaRouter.class, TecnologiaHandler.class,
        TecnologiaMapperImpl.class, BeanConfiguration.class})
class TecnologiaRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ITecnologiaServicePort servicePort;

    @Test
    void registrar_exitoso_retorna201() {
        Tecnologia guardada = new Tecnologia(1L, "Java", "Lenguaje backend");
        when(servicePort.registrar(any())).thenReturn(Mono.just(guardada));

        webTestClient.post()
                .uri("/api/v1/tecnologias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TecnologiaRequest("Java", "Lenguaje backend"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Java")
                .jsonPath("$.descripcion").isEqualTo("Lenguaje backend");
    }

    @Test
    void registrar_nombreDuplicado_retorna400() {
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new TecnologiaException("TEC-005",
                        "Ya existe una tecnología con ese nombre")));

        webTestClient.post()
                .uri("/api/v1/tecnologias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TecnologiaRequest("Java", "Lenguaje backend"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("TEC-005");
    }

    @Test
    void registrar_errorInterno_retorna500() {
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new RuntimeException("Error de BD")));

        webTestClient.post()
                .uri("/api/v1/tecnologias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TecnologiaRequest("Java", "Desc"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.code").isEqualTo("TEC-500");
    }

    @Test
    void listar_retorna200ConLista() {
        when(servicePort.listarTodas()).thenReturn(Flux.just(
                new Tecnologia(1L, "Java", "Lenguaje backend"),
                new Tecnologia(2L, "Spring", "Framework Java")
        ));

        webTestClient.get()
                .uri("/api/v1/tecnologias")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class)
                .hasSize(2);
    }

    @Test
    void listar_listaVacia_retorna200() {
        when(servicePort.listarTodas()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/v1/tecnologias")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class)
                .hasSize(0);
    }

    @Test
    void buscarPorId_existe_retorna200() {
        when(servicePort.buscarPorId(1L))
                .thenReturn(Mono.just(new Tecnologia(1L, "Java", "Lenguaje backend")));

        webTestClient.get()
                .uri("/api/v1/tecnologias/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Java");
    }

    @Test
    void buscarPorId_noExiste_retorna404() {
        when(servicePort.buscarPorId(anyLong())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/tecnologias/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void eliminar_exitoso_retorna204() {
        when(servicePort.eliminarPorId(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/tecnologias/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void eliminar_noExiste_retorna404() {
        when(servicePort.eliminarPorId(anyLong()))
                .thenReturn(Mono.error(new RuntimeException("No encontrado")));

        webTestClient.delete()
                .uri("/api/v1/tecnologias/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}