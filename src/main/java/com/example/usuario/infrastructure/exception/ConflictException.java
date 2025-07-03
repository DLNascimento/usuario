package com.example.usuario.infrastructure.exception;


public class ConflictException extends RuntimeException {

    public ConflictException(String mensagem){
        super(mensagem);
    }
    public ConflictException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
