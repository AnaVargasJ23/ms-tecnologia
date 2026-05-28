package com.onclass.tecnologia.infrastructure.entrypoints;

import com.onclass.tecnologia.application.handler.TecnologiaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TecnologiaRouter {

    @Bean
    public RouterFunction<ServerResponse> tecnologiaRoutes(TecnologiaHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/tecnologias", handler::registrar)
                .GET("/api/v1/tecnologias", handler::listar)
                .build();
    }
}