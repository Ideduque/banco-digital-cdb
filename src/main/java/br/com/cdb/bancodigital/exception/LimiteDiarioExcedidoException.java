package br.com.cdb.bancodigital.exception;

public class LimiteDiarioExcedidoException extends RuntimeException
{
    // Construtor que recebe uma mensagem de erro
    public LimiteDiarioExcedidoException(String mensagem)
    {
        super(mensagem); // Passa a mensagem para a superclasse RuntimeException
    }
}
