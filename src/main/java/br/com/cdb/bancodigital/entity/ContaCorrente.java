package br.com.cdb.bancodigital.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class ContaCorrente extends Conta {

    @Override
    public void aplicarMensalidadeOuRendimento() {

    }

    @Override
    public void processarMensalidade()
    {
        BigDecimal taxa;

        switch (cliente.getCategoria())
        {
            case COMUM -> taxa = new BigDecimal("12.00");
            case SUPER -> taxa = new BigDecimal("8.00");
            case PREMIUM -> taxa = BigDecimal.ZERO;
            default -> throw new IllegalArgumentException("Categoria inválida");
        }

        this.saldo = saldo.subtract(taxa);
    }
}
