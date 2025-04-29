package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoDebitoDTO
{
    @NotNull(message = "O ID da conta não pode ser nulo")
    private Long contaId;

    @NotNull(message = "A senha não pode ser nula")
    private String senha;

    @Positive(message = "O limite diário deve ser um valor positivo")
    private BigDecimal limiteDiario;
}
