package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity // Indica que esta classe é uma entidade JPA (vai virar uma tabela no banco)
@Inheritance(strategy = InheritanceType.JOINED) // Define a estratégia de herança entre entidades: cada classe filha terá sua própria tabela, mas relacionada à tabela "conta"
@Data // Lombok: gera automaticamente getters, setters, equals, hashCode e toString
@NoArgsConstructor
@AllArgsConstructor // Lombok: gera construtor com todos os argumentos
public abstract class Conta
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id; // Identificador único da conta, gerado automaticamente pelo banco

    @ManyToOne // Muitas contas podem pertencer a um único cliente
    @JoinColumn(name = "cliente_id") // Nome da coluna no banco que liga à tabela cliente
    protected Cliente cliente;

    private String numero;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;  // Define o tipo da conta (CORRENTE ou POUPANÇA)

    // Saldo da conta, iniciado com zero. BigDecimal é o tipo ideal para valores monetários
    protected BigDecimal saldo = BigDecimal.ZERO;

    public void depositar(BigDecimal valor)
    {
        saldo = saldo.add(valor);
    }

    public boolean sacar(BigDecimal valor)
    {
        if (saldo.compareTo(valor) >= 0) { // Verifica se o saldo é suficiente
            saldo = saldo.subtract(valor);
            return true;
        }
        return false;
    }

    // Método abstrato para mensalidade, será implementado nas subclasses
    public abstract void processarMensalidade() throws SaldoInsuficienteException;


    public boolean transferir(Conta contaDestino, BigDecimal valor)
    {
        if (this.sacar(valor)) { // Tenta sacar o valor da conta de origem
            contaDestino.depositar(valor); // Se o saque for bem-sucedido, deposita na conta destino
            return true;
        }
        return false; // Se não houver saldo suficiente, retorna false
    }

}