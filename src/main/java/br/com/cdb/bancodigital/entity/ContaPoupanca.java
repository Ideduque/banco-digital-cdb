package br.com.cdb.bancodigital.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaPoupanca extends Conta {


    @Override
    public void aplicarMensalidadeOuRendimento()
    {
        if (cliente == null || cliente.getCategoria() == null)
        {
            throw new IllegalStateException("Cliente ou categoria não pode ser nulo.");
        }

    }

    @Override
    public void processarMensalidade() {
        BigDecimal taxaAnual;

        switch (cliente.getCategoria()) {
            case COMUM -> taxaAnual = new BigDecimal("0.005");
            case SUPER -> taxaAnual = new BigDecimal("0.007");
            case PREMIUM -> taxaAnual = new BigDecimal("0.009");
            default -> throw new IllegalArgumentException("Categoria inválida");
        }

        // juros compostos mensais
        BigDecimal rendimentoMensal = saldo.multiply(taxaAnual).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        this.saldo = saldo.add(rendimentoMensal);
    }
}
