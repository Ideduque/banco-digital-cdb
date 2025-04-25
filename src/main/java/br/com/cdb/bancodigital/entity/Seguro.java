package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.TipoSeguro;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroApolice;

    private LocalDate dataContratacao;

    private BigDecimal valor;

    private String condicoes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;

    @Enumerated(EnumType.STRING)
    private TipoSeguro tipoSeguro;
}

