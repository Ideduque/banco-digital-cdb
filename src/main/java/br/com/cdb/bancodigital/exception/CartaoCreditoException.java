package br.com.cdb.bancodigital.exception;

import lombok.Getter;

@Getter
public abstract class CartaoCreditoException extends RuntimeException
{
    private final String message;
    private final int statusCode;

    public CartaoCreditoException(String message, int statusCode)
    {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

}
