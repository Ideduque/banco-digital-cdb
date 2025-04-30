package br.com.cdb.bancodigital.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoCreditoDTO
{
    private Long cartaoId;
    private String senha;
    private BigDecimal valor;
}
