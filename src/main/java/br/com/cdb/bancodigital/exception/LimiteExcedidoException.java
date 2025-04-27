package br.com.cdb.bancodigital.exception;

public class LimiteExcedidoException extends RuntimeException
{
    // Construtor que recebe uma mensagem
    public LimiteExcedidoException(String message)
    {
        super(message);
    }
}
