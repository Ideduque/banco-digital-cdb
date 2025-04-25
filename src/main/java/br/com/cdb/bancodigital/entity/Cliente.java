package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato do CPF inválido")
    @Column(unique = true)
    private String cpf;

    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    private String nome;

    @NotNull
    private LocalDate dataNascimento;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

}


