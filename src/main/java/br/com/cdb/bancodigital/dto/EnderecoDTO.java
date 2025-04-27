package br.com.cdb.bancodigital.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoDTO
{
    @NotBlank(message = "Rua não pode ser vazia")
    private String rua;

    @NotBlank(message = "Número não pode ser vazio")
    private String numero;

    private String complemento; // Pode ser opcional

    @NotBlank(message = "Cidade não pode ser vazia")
    private String cidade;

    @NotBlank(message = "Estado não pode ser vazio")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "Estado deve ter apenas duas letras (sigla)")
    private String estado;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;
}
