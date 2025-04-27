package br.com.cdb.bancodigital.exception;

public class CartaoNaoEncontradoException extends RuntimeException
{
    // Construtor que recebe uma mensagem
    public CartaoNaoEncontradoException(String message)
    {
        super(message);
    }
}
