package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ContaCorrente extends Conta
{
    private BigDecimal limite;

    //Define o limite da conta corrente com base na categoria do cliente.
    public void definirLimitePorCategoria()
    {
        if (this.cliente == null || this.cliente.getCategoria() == null)
        {
            throw new IllegalStateException("Cliente ou categoria não definida");
        }

        switch (this.cliente.getCategoria())
        {
            case COMUM -> this.limite = new BigDecimal("500.00");
            case SUPER -> this.limite = new BigDecimal("1000.00");
            case PREMIUM -> this.limite = new BigDecimal("2000.00");
            default -> throw new IllegalArgumentException("Categoria inválida");
        }
    }

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
