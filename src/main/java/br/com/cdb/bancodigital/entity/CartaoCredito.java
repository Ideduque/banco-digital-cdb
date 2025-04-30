package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.exception.LimiteCreditoExcedidoException;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Entity
//@DiscriminatorValue("CREDITO") // Valor da coluna discriminadora na herança SINGLE_TABLE
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // Necessário para herança com Lombok
public class CartaoCredito extends Cartao
{
    // Limite total de crédito aprovado para o cartão
    @Column(nullable = false)
    private BigDecimal limiteAprovado = BigDecimal.ZERO;

    // Valor já utilizado do limite
    @Column(nullable = false)
    private BigDecimal valorUtilizado = BigDecimal.ZERO;

    // Implementação do método abstrato herdado da classe Cartao
    // Representa um pagamento feito com o cartão de crédito
    @Override
    public boolean pagar(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Valor inválido");
        }

        // Calcula o novo total utilizado
        BigDecimal novoUtilizado = this.valorUtilizado.add(valor);

        // Verifica se o novo valor ultrapassa o limite aprovado
        if (novoUtilizado.compareTo(this.limiteAprovado) > 0)
        {
            return false; // Pagamento recusado: limite excedido
        }

        // Atualiza o valor utilizado
        this.valorUtilizado = novoUtilizado;
        return true; // Pagamento aprovado
    }

    // Reduz o valor utilizado (ex: pagamento de fatura)
    public void reduzirValorUtilizado(BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
        this.valorUtilizado = this.valorUtilizado.subtract(valor);
    }

    // Método que paga a fatura do cartão de crédito
    public void pagarFatura(BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Valor de pagamento deve ser positivo.");
        }

        // Verifica se o saldo utilizado é menor que o valor da fatura
        if (this.getSaldoUtilizado().compareTo(valor) > 0)
        {
            // Reduz o saldo utilizado do cartão
            this.reduzirSaldoUtilizado(valor);
            log.info("Fatura do Cartão de Crédito paga: ID={} | Valor={}", this.getId(), valor);
        } else {
            throw new LimiteCreditoExcedidoException("Valor da fatura não pode ser maior que o saldo utilizado.");
        }
    }
}
