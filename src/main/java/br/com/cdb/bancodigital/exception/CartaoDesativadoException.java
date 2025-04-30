package br.com.cdb.bancodigital.exception;

public class CartaoDesativadoException extends CartaoCreditoException
{
    public CartaoDesativadoException(String message)

    {
        super(message, 400);  // Código HTTP 400 (Bad Request)
    }
}
