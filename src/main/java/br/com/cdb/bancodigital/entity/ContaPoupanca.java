package br.com.cdb.bancodigital.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class ContaPoupanca extends Conta
{
    @Override
    public void processarMensalidade() //Aplica rendimento mensal com base na categoria do cliente.
    {
        BigDecimal taxaAnual;

        switch (cliente.getCategoria())
        {
            case COMUM -> taxaAnual = new BigDecimal("0.005");
            case SUPER -> taxaAnual = new BigDecimal("0.007");
            case PREMIUM -> taxaAnual = new BigDecimal("0.009");
            default -> throw new IllegalArgumentException("Categoria inválida");
        }

        // Calcula o rendimento mensal equivalente usando juros compostos simples:
        // rendimento = saldo * (taxaAnual / 12)
        BigDecimal rendimentoMensal = saldo
                .multiply(taxaAnual)
                .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        // Adiciona o rendimento ao saldo atual
        this.saldo = saldo.add(rendimentoMensal);
    }
}
