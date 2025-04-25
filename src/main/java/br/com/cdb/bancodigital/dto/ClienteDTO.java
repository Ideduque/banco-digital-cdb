package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.enums.Categoria;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ClienteDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato do CPF inválido")
    private String cpf;

    @NotBlank
    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    private String nome;

    @NotNull
    private LocalDate dataNascimento;

    @NotNull
    private EnderecoDTO endereco;

    @NotNull
    private Categoria categoria;
}

