package com.onclass.tecnologia.infrastructure.entrypoints;

import com.onclass.tecnologia.application.handler.TecnologiaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Tecnología", description = "Gestión de tecnologías del sistema On-Class")
public class TecnologiaRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/tecnologias",
                    method = RequestMethod.POST,
                    beanClass = TecnologiaHandler.class,
                    beanMethod = "registrar",
                    operation = @Operation(
                            operationId = "registrarTecnologia",
                            summary = "Registrar una nueva tecnología",
                            tags = {"Tecnología"},
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Tecnología creada exitosamente"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/tecnologias",
                    method = RequestMethod.GET,
                    beanClass = TecnologiaHandler.class,
                    beanMethod = "listar",
                    operation = @Operation(
                            operationId = "listarTecnologias",
                            summary = "Listar todas las tecnologías",
                            tags = {"Tecnología"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de tecnologías")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> tecnologiaRoutes(TecnologiaHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/tecnologias", handler::registrar)
                .GET("/api/v1/tecnologias", handler::listar)
                .build();
    }
}