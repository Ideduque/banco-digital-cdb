package br.com.cdb.bancodigital.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class PixDTO {
    private Long contaOrigemId;
    private Long contaDestinoId;
    private BigDecimal valor;
}
