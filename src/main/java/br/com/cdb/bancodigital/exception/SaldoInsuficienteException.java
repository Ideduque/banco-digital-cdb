package br.com.cdb.bancodigital.exception;

public class SaldoInsuficienteException extends Throwable
{
    public SaldoInsuficienteException(String mensagem)
    {
        super(mensagem);
    }
}
