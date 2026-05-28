package com.onclass.tecnologia.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TecnologiaErrorEnum {

    NOMBRE_OBLIGATORIO("TEC-001", "El nombre es obligatorio"),
    NOMBRE_MAX_50("TEC-002", "El nombre no puede superar 50 caracteres"),
    DESCRIPCION_OBLIGATORIA("TEC-003", "La descripción es obligatoria"),
    DESCRIPCION_MAX_90("TEC-004", "La descripción no puede superar 90 caracteres"),
    NOMBRE_DUPLICADO("TEC-005", "Ya existe una tecnología con ese nombre");

    private final String code;
    private final String message;
}