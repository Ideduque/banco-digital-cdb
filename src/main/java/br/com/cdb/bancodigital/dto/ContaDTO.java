package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class ContaDTO
{
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotBlank(message = "O tipo da conta é obrigatório (CORRENTE ou POUPANCA)")
    private String tipo;
}
