package br.com.cdb.bancodigital.entity;

// Importações de pacotes necessários
import br.com.cdb.bancodigital.enums.Categoria; // Enum que define a categoria do cliente (ex: COMUM, SUPER, PREMIUM)
import jakarta.persistence.*; // Anotações JPA para persistência de dados (Hibernate)
import jakarta.validation.constraints.*; // Anotações de validação de dados
import lombok.*; // Lombok para gerar código padrão automaticamente (getters, setters, construtores)

import java.time.LocalDate; // Usado para trabalhar com datas (nascimento)

// Marca a classe como uma entidade JPA que será mapeada para uma tabela no banco de dados
@Entity
// Gera automaticamente getters, setters, toString, equals e hashCode (Lombok)
@Data
// Gera um construtor vazio (obrigatório para JPA)
@NoArgsConstructor
// Gera um construtor com todos os campos
@AllArgsConstructor
public class Cliente
{
    // Identificador único da entidade, chave primária no banco
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CPF do cliente, não pode ser vazio, deve seguir o formato correto, e precisa ser único no banco
    @NotBlank
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato do CPF inválido")
    @Column(unique = true)
    private String cpf;

    // Nome do cliente, obrigatório, entre 2 e 100 caracteres, apenas letras e espaços permitidos
    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nome inválido: use apenas letras e espaços")
    private String nome;

    // Data de nascimento do cliente, obrigatória
    @NotNull
    private LocalDate dataNascimento;

    // Endereço do cliente, classe separada e embutida no Cliente
    @Embedded
    private Endereco endereco;

    // Categoria do cliente, armazenada como string no banco (COMUM, SUPER, PREMIUM)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
}