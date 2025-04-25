package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Extrato
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataTransacao;
    private String descricao;
    private BigDecimal valor;

    @ManyToOne
    private Conta contaBancaria;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

}

