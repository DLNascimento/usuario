package com.example.usuario.infrastructure.exception;

public class ResourceNotFound extends RuntimeException{

    public ResourceNotFound(String mensagem){
        super(mensagem);
    }
    public ResourceNotFound(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
