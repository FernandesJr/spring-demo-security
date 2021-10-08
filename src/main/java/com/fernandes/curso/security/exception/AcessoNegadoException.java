package com.fernandes.curso.security.exception;

//Exception lançada em tempo de execução
public class AcessoNegadoException extends RuntimeException{

    public AcessoNegadoException(String message) {
        super(message);
    }
}
