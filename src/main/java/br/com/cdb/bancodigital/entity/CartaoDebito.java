package br.com.cdb.bancodigital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoDebito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private boolean ativo = true;
    private String senha;
    private BigDecimal limiteDiario = BigDecimal.valueOf(1000);
    private BigDecimal gastoHoje = BigDecimal.ZERO;
    private String dataUltimoUso;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;
}
