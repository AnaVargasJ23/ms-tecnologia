package com.onclass.tecnologia.domain.excepcion;

import lombok.Getter;

@Getter
public class TecnologiaException extends RuntimeException {

    private final String code;
    private final String message;

    public TecnologiaException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
