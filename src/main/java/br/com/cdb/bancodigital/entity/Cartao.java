package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.TipoCartao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cartao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Cartao {

    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne(optional = false)
    private Conta conta;

    @NotBlank
    private String senha;

    public void alterarSenha(String novaSenha)
    {
        this.senha = novaSenha;
    }

    private boolean ativo;

    public abstract boolean pagar(BigDecimal valor);

    private String numero;

    private BigDecimal limiteCredito; // Somente para Cartão de Crédito
    private BigDecimal limiteDiario; // Somente para Cartão de Débito
    private BigDecimal saldoUtilizado; // Apenas para Cartão de Crédito

    @Enumerated(EnumType.STRING)
    private TipoCartao tipoCartao;
}

