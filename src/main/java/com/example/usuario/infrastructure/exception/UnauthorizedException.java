package com.example.usuario.infrastructure.exception;


import javax.security.sasl.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String mensagem) {
        super(mensagem);
    }

    public UnauthorizedException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }

}
