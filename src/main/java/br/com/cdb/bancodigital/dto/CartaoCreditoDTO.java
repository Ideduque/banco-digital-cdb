package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartaoCreditoDTO {

    @NotNull(message = "O ID da conta é obrigatório")  // Valida que o ID da conta não seja nulo
    private Long contaId;

    @NotNull(message = "A senha é obrigatória")  // Valida que a senha não seja nula
    private String senha;

    @NotNull(message = "O limite de crédito é obrigatório")  // Valida que o limite não seja nulo
    private BigDecimal limite;

    // Construtor adicional (opcional) para facilitar a criação do DTO de forma controlada
    public CartaoCreditoDTO(Long contaId, String senha, BigDecimal limite) {
        this.contaId = contaId;
        this.senha = senha;
        this.limite = limite;
    }
}
