package br.com.cdb.bancodigital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartaoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private boolean ativo = true;
    private String senha;

    private BigDecimal limiteAprovado;
    private BigDecimal valorUtilizado;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;
}