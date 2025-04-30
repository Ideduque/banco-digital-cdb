package br.com.cdb.bancodigital.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import java.math.BigDecimal;

@Entity
//@DiscriminatorValue("DEBITO") // Define o valor salvo na coluna tipo_cartao do banco de dados
@Data
public class CartaoDebito extends Cartao
{
    private BigDecimal limiteDiario;
    private BigDecimal saldoUtilizado;
    private BigDecimal gastoHoje = BigDecimal.ZERO;
    private String dataUltimoUso;
    private String senha;

    // Construtor usado na emissão de um novo cartão de débito
    public CartaoDebito(Conta conta, String senha, String numero, BigDecimal limiteDiario)
    {
        super(); // Chama o construtor da classe Cartao para inicializar atributos comuns

        // Define o tipo do cartão como Débito
        this.setTipoCartao(br.com.cdb.bancodigital.enums.TipoCartao.DEBITO);

        // Define o limite diário do cartão de débito
        this.setLimiteDiario(limiteDiario);

        // Inicializa o saldo utilizado com 0, pois o cartão ainda não foi usado
        this.setSaldoUtilizado(BigDecimal.ZERO);

        // Marca o cartão como ativo
        this.setAtivo(true);
    }

    // Construtor vazio exigido pelo JPA
    public CartaoDebito()

    {
        super(); // Chama o construtor vazio da classe Cartao
    }

    // Método para realizar um pagamento utilizando o cartão de débito
    @Override
    public boolean pagar(BigDecimal valor)
    {
        // Verifica se o cartão está ativo, se não estiver, o pagamento é negado
        if (!this.isAtivo())
        {
            return false; // Cartão inativo não pode realizar pagamentos
        }

        // Verifica o saldo utilizado no cartão, se for nulo, inicializa com zero
        BigDecimal saldoAtual = this.getSaldoUtilizado() != null ? this.getSaldoUtilizado() : BigDecimal.ZERO;

        // Calcula o novo saldo após o pagamento
        BigDecimal novoSaldo = saldoAtual.add(valor);

        // Verifica se o novo saldo ultrapassa o limite diário do cartão
        if (novoSaldo.compareTo(this.getLimiteDiario()) > 0)
        {
            return false; // O pagamento não pode ser realizado se ultrapassar o limite
        }

        // Atualiza o saldo utilizado com o novo valor após o pagamento
        this.setSaldoUtilizado(novoSaldo);

        // Retorna true indicando que o pagamento foi autorizado
        return true;
    }
}