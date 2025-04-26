package br.com.cdb.bancodigital.Exception;

public class SaldoInsuficienteException extends Throwable
{
    public SaldoInsuficienteException(String mensagem)
    {
        super(mensagem);
    }
}
