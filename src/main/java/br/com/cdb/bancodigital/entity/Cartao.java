package br.com.cdb.bancodigital.entity;

// Importação de dependências necessárias
import br.com.cdb.bancodigital.enums.TipoCartao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // Usando a estratégia SINGLE_TABLE para armazenar diferentes tipos de cartão na mesma tabela
@DiscriminatorColumn(name = "tipo_cartao")  // Coluna para discriminar os tipos de cartão
@Data  // Lombok: gera automaticamente os métodos getters, setters, equals, hashCode e toString
@NoArgsConstructor  // Lombok: gera o construtor sem parâmetros
@AllArgsConstructor  // Lombok: gera o construtor com todos os parâmetros
@SuperBuilder
public abstract class Cartao {

    @Id
    private String id = UUID.randomUUID().toString();  // Geração automática de ID utilizando UUID

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_id")  // Relacionamento com a conta associada ao cartão
    private Conta conta;

    @NotBlank
    @Setter(AccessLevel.PROTECTED)  // A senha é alterada apenas pelo método protegido
    private String senha;

    @Setter  // Setter para o campo ativo
    private boolean ativo;

    @Column(nullable = false, unique = true)  // Garantindo que o número do cartão seja único
    private String numero;

    @Setter(AccessLevel.PROTECTED)  // Setter protegido para o limite de crédito
    private BigDecimal limiteCredito = BigDecimal.ZERO;  // Definindo o limite de crédito inicial como zero

    @Setter(AccessLevel.PROTECTED)  // Setter protegido para o limite diário
    private BigDecimal limiteDiario = BigDecimal.ZERO;  // Definindo o limite diário inicial como zero

    @Setter(AccessLevel.PROTECTED)  // Setter protegido para o saldo utilizado
    private BigDecimal saldoUtilizado = BigDecimal.ZERO;  // Inicializando o saldo utilizado como zero

    @Enumerated(EnumType.STRING)  // Utilizando Enum para os tipos de cartão
    private TipoCartao tipoCartao;

    // Construtor para criação de um novo cartão, recebendo conta, senha, número e tipo do cartão
    public Cartao(Conta conta, String senha, String numero, TipoCartao tipoCartao) {
        this.conta = conta;
        this.senha = senha;
        this.numero = numero;
        this.tipoCartao = tipoCartao;
        this.ativo = true;  // O cartão é ativo por padrão
    }

    // Método executado antes de persistir a entidade, garantindo que o ID seja gerado se não estiver presente
    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();  // Geração do ID se não definido
        }
    }

    // Método para alterar a senha do cartão
    public void alterarSenha(String novaSenha)
    {
        if (novaSenha == null || novaSenha.isBlank())
        {
            throw new IllegalArgumentException("Nova senha não pode ser vazia.");  // Validando senha não vazia
        }
        if (novaSenha.length() < 8)
        {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");  // Senha deve ter ao menos 8 caracteres
        }
        this.senha = novaSenha;
    }

    // Método abstrato que deve ser implementado nas subclasses para realizar o pagamento
    public abstract boolean pagar(BigDecimal valor);

    // Método para adicionar saldo utilizado ao cartão
    public void adicionarSaldoUtilizado(BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Valor deve ser positivo.");  // Verificando se o valor é positivo
        }
        if (this.saldoUtilizado.add(valor).compareTo(this.limiteCredito) > 0)
        {
            throw new IllegalArgumentException("Saldo utilizado excede o limite de crédito.");  // Verificando se o saldo ultrapassa o limite de crédito
        }
        this.saldoUtilizado = saldoUtilizado.add(valor);  // Atualizando o saldo utilizado
    }

    // Método para reduzir o saldo utilizado pelo valor informado
    public void reduzirSaldoUtilizado(BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Valor deve ser positivo.");  // Verificando se o valor é positivo
        }
        if (this.saldoUtilizado.subtract(valor).compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("O saldo utilizado não pode ser negativo.");  // Garantindo que o saldo não se torne negativo
        }
        this.saldoUtilizado = saldoUtilizado.subtract(valor);  // Atualizando o saldo utilizado
    }
}
