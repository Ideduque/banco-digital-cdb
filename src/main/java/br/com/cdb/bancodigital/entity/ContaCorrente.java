package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.Categoria;
import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ContaCorrente extends Conta
{
    /** Inicializa um logger estático para esta classe, permitindo registrar mensagens de log.
     (info/debug/error) associadas ao contexto de ContaCorrente via SLF4J.
     O nome do logger será o nome completo da classe, facilitando o rastreamento da origem das mensagens. **/
    private static final Logger log = LoggerFactory.getLogger(ContaCorrente.class);

    // Constantes para limites por categoria
    private static final BigDecimal LIMITE_COMUM   = new BigDecimal("500.00");
    private static final BigDecimal LIMITE_SUPER   = new BigDecimal("1000.00");
    private static final BigDecimal LIMITE_PREMIUM = new BigDecimal("2000.00");

    // Mapa de limites para cada categoria
    private static final Map<Categoria, BigDecimal> LIMITES = Map.of(
            Categoria.COMUM,   LIMITE_COMUM,
            Categoria.SUPER,   LIMITE_SUPER,
            Categoria.PREMIUM, LIMITE_PREMIUM
    );

    private BigDecimal limite;

     //Define o limite de crédito de acordo com a categoria do cliente.
    public void definirLimitePorCategoria() {
        if (cliente == null || cliente.getCategoria() == null)
        {
            throw new IllegalStateException("Cliente ou categoria não definida");
        }

        BigDecimal novoLimite = LIMITES.get(cliente.getCategoria());
        if (novoLimite == null)
        {
            throw new IllegalArgumentException("Categoria inválida: " + cliente.getCategoria());
        }

        this.limite = novoLimite;
        log.info("ContaCorrente ID={} — limite definido para {}", getId(), limite);
    }

     //Cobra mensalidade de acordo com a categoria do cliente.
    @Override
    public void processarMensalidade() throws SaldoInsuficienteException
    {
        if (cliente == null)
        {
            throw new IllegalStateException("Cliente não associado à conta");
        }

        // Constantes para taxa de mensalidade
        final BigDecimal TAXA_COMUM    = new BigDecimal("12.00");
        final BigDecimal TAXA_SUPER    = new BigDecimal("8.00");
        final BigDecimal TAXA_PREMIUM  = BigDecimal.ZERO;

        BigDecimal taxa = switch (cliente.getCategoria())
        {
            case COMUM    -> TAXA_COMUM;
            case SUPER    -> TAXA_SUPER;
            case PREMIUM  -> TAXA_PREMIUM;
        };

        // Verifica saldo
        if (saldo.compareTo(taxa) < 0)
        {
            throw new SaldoInsuficienteException("Saldo insuficiente para cobrança da mensalidade.");
        }

        // Log antes de debitar
        log.info("ContaCorrente ID={} — saldo antes={}, taxa={}", getId(), saldo, taxa);

        // Atualiza saldo
        this.saldo = saldo.subtract(taxa);

        // Log após debitar
        log.info("ContaCorrente ID={} — novo saldo={}", getId(), saldo);
    }
}