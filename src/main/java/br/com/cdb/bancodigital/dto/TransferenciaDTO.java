package br.com.cdb.bancodigital.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferenciaDTO
{
    private Long contaOrigemId;
    private Long contaDestinoId;
    private BigDecimal valor;
}
