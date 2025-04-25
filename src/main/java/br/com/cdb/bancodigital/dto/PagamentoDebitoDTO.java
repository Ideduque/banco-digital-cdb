package br.com.cdb.bancodigital.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagamentoDebitoDTO {
    private Long cartaoId;
    private String senha;
    private BigDecimal valor;
}
