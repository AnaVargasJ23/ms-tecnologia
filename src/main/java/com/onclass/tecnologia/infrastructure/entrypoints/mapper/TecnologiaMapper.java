package com.onclass.tecnologia.infrastructure.entrypoints.mapper;

import com.onclass.tecnologia.domain.model.Tecnologia;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.TecnologiaRequest;
import com.onclass.tecnologia.infrastructure.entrypoints.dto.TecnologiaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TecnologiaMapper {

    @Mapping(target = "id", ignore = true)
    Tecnologia toDomain(TecnologiaRequest request);

    TecnologiaResponse toResponse(Tecnologia tecnologia);
}