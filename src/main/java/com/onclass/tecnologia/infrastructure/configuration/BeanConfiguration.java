package com.onclass.tecnologia.infrastructure.configuration;

import com.onclass.tecnologia.domain.api.ITecnologiaServicePort;
import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import com.onclass.tecnologia.domain.usecase.TecnologiaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ITecnologiaServicePort tecnologiaServicePort(ITecnologiaPersistencePort persistencePort) {
        return new TecnologiaUseCase(persistencePort);
    }
}