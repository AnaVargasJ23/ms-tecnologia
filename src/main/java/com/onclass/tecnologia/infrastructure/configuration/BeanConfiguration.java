package com.onclass.tecnologia.infrastructure.configuration;

import com.onclass.tecnologia.domain.spi.ITecnologiaPersistencePort;
import com.onclass.tecnologia.domain.usecase.TecnologiaUseCase;
import com.onclass.tecnologia.infrastructure.adapters.persistence.TecnologiaPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TecnologiaUseCase tecnologiaUseCase(ITecnologiaPersistencePort persistencePort) {
        return new TecnologiaUseCase(persistencePort);
    }
}