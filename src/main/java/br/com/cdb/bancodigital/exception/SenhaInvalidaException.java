package br.com.cdb.bancodigital.exception;

public class SenhaInvalidaException extends CartaoCreditoException
{
    public SenhaInvalidaException(String message)
    {
        super(message, 400);  // Código HTTP 400 (Bad Request)
    }
}