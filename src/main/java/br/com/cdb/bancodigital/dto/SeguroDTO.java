package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.enums.TipoSeguro;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeguroDTO
{
    private String cartaoCreditoId;
    private TipoSeguro tipoSeguro;
    private BigDecimal valor;
    private String condicoes;
}

