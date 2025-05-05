package br.com.cdb.bancodigital.entity;

import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cdb.bancodigital.enums.Categoria;

@Entity
public class ContaPoupanca extends Conta
{
    private static final Logger log = LoggerFactory.getLogger(ContaPoupanca.class);

    // Constantes para taxas anuais por categoria
    private static final BigDecimal TAXA_COMUM    = new BigDecimal("0.005");
    private static final BigDecimal TAXA_SUPER    = new BigDecimal("0.007");
    private static final BigDecimal TAXA_PREMIUM  = new BigDecimal("0.009");
    private static final BigDecimal MESES_NO_ANO   = new BigDecimal("12");

    // Map que associa cada categoria à sua taxa anual
    private static final Map<Categoria, BigDecimal> TAXAS = Map.of(
            Categoria.COMUM,   TAXA_COMUM,
            Categoria.SUPER,   TAXA_SUPER,
            Categoria.PREMIUM, TAXA_PREMIUM
    );

    // Aplica o rendimento mensal à conta poupança com base na categoria do cliente.
    @Override
    public void processarMensalidade()
    {
        // Valida pré-condições
        if (cliente == null)
        {
            throw new IllegalStateException("Cliente não associado à conta");
        }
        if (saldo == null)
        {
            throw new IllegalStateException("Saldo não inicializado");
        }

        // Captura valores em variáveis locais
        BigDecimal saldoAtual = this.saldo;
        Categoria categoria   = cliente.getCategoria();

        // Busca taxa no mapa; lança se categoria inválida
        BigDecimal taxaAnual = TAXAS.get(categoria);
        if (taxaAnual == null) {
            throw new IllegalArgumentException("Categoria inválida: " + categoria);
        }

        // Calcula rendimento mensal: (saldo * taxaAnual) / 12
        BigDecimal rendimentoMensal = saldoAtual
                .multiply(taxaAnual)
                .divide(MESES_NO_ANO, 2, RoundingMode.HALF_UP);

        // Log antes de aplicar
        log.info("ContaPoupanca ID={} — categoria={}, saldoAntes={}, rendimento={}",
                getId(), categoria, saldoAtual, rendimentoMensal);

        // Atualiza saldo
        this.saldo = saldoAtual.add(rendimentoMensal);

        // Log depois de aplicar
        log.info("ContaPoupanca ID={} — novoSaldo={}", getId(), this.saldo);
    }
}
