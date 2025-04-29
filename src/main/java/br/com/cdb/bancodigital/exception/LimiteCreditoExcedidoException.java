package br.com.cdb.bancodigital.exception;

public class LimiteCreditoExcedidoException extends CartaoCreditoException
{

  public LimiteCreditoExcedidoException(String message)
  {
    super(message, 400);  // Código HTTP 400 (Bad Request)
  }
}