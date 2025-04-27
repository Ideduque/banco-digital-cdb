package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.enums.Categoria;
import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class ClienteDTO
{
    // Identificador único do cliente (pode ser usado na resposta, mas geralmente não na criação)
    private Long id;

    // CPF do cliente, obrigatório, validado por formato
    @NotBlank // Não pode ser nulo ou vazio
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato do CPF inválido") // Formato obrigatório: 000.000.000-00
    private String cpf;

    // Nome do cliente, obrigatório, entre 2 e 100 caracteres, apenas letras e espaços
    @NotBlank
    @Size(min = 2, max = 100) // Tamanho mínimo e máximo permitido para o nome
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços") // Apenas letras e espaços permitidos
    private String nome;

    // Data de nascimento, obrigatória
    @NotNull
    private LocalDate dataNascimento;

    // Endereço do cliente, obrigatório, usando outro DTO para organização
    @NotNull
    private EnderecoDTO endereco;

    // Categoria do cliente (COMUM, SUPER, PREMIUM), obrigatória
    @NotNull
    private Categoria categoria;
}
