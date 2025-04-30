package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class ContaCorrente extends Conta
{
    @Override
    public void processarMensalidade() throws SaldoInsuficienteException
    {
        BigDecimal taxa;

        // Define o valor da taxa de mensalidade de acordo com a categoria do cliente:
        switch (cliente.getCategoria())
        {
            case COMUM -> taxa = new BigDecimal("12.00");
            case SUPER -> taxa = new BigDecimal("8.00");
            case PREMIUM -> taxa = BigDecimal.ZERO;
            default -> throw new IllegalArgumentException("Categoria inválida");
        }

        // Verifica se há saldo suficiente antes de cobrar a taxa
        if (saldo.compareTo(taxa) < 0)
        {
            throw new SaldoInsuficienteException("Saldo insuficiente para cobrança da mensalidade.");
        }

        // Subtrai a taxa do saldo da conta
        this.saldo = saldo.subtract(taxa);
    }
}
