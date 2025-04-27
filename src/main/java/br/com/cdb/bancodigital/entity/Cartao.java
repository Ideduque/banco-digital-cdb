package br.com.cdb.bancodigital.entity;

// Importação de dependências necessárias
import br.com.cdb.bancodigital.enums.TipoCartao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;  // Classe para manipulação de números decimais de alta precisão (usado para valores monetários)
import java.util.UUID;  // Classe para gerar identificadores únicos (UUID)

@Entity  // Define que esta classe é uma entidade JPA, ou seja, mapeada para uma tabela no banco de dados
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // Estratégia de herança em JPA, usando uma única tabela para todas as subclasses
@DiscriminatorColumn(name = "tipo_cartao")  // Define a coluna de discriminação que será usada para diferenciar as subclasses (Cartão de Crédito, Débito, etc.)
@Data  // Lombok: Gera automaticamente os métodos getters, setters, equals, hashCode e toString
@NoArgsConstructor  // Lombok: Gera um construtor sem parâmetros
@AllArgsConstructor  // Lombok: Gera um construtor com todos os parâmetros

// Classe abstrata Cartao, que será a base para as classes CartaoCredito e CartaoDebito
public abstract class Cartao
{
    @Id  // Indica que o campo 'id' é a chave primária na tabela
    private String id = UUID.randomUUID().toString();  // Gera automaticamente um UUID único como ID do cartão

    @ManyToOne(optional = false)  // Relacionamento (muitos para um) com a entidade Conta (cartão pertence a uma conta)
    private Conta conta;  // Conta associada ao cartão

    @NotBlank  // Validação para garantir que o campo 'senha' não seja vazio
    private String senha;  // Senha do cartão

    // Método para alterar a senha do cartão
    public void alterarSenha(String novaSenha)
    {
        this.senha = novaSenha;  // A senha do cartão é atualizada
    }

    private boolean ativo;  // Indica se o cartão está ativo ou não (pode ser usado para bloqueio do cartão)

    // Método abstrato que será implementado nas subclasses, para realizar um pagamento com o cartão
    public abstract boolean pagar(BigDecimal valor);  // Lógica de pagamento, a ser definida em subclasses

    private String numero;  // Número do cartão (em formato padrão)

    private BigDecimal limiteCredito;  // Somente para Cartão de Crédito, define o limite de crédito disponível

    private BigDecimal limiteDiario;  // Somente para Cartão de Débito, define o limite diário de transações

    private BigDecimal saldoUtilizado;  // Somente para Cartão de Crédito, define o saldo utilizado do limite de crédito

    @Enumerated(EnumType.STRING)  // Define que o tipoCartao será armazenado como uma string no banco de dados
    private TipoCartao tipoCartao;  // Tipo de cartão (Crédito ou Débito) chamando de um ENUM
}