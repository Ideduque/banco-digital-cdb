package br.com.cdb.bancodigital.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoCreditoDTO {
    private Long contaId;
    private String senha;
    private BigDecimal limite;
}
