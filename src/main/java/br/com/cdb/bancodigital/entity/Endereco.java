package br.com.cdb.bancodigital.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Embeddable
@Data
public class Endereco
{
    @NotBlank(message = "Rua não pode ser vazia") // Garante que a rua não seja nula ou vazia
    private String rua;

    @NotBlank(message = "Número não pode ser vazio")
    private String numero;

    // Complemento é opcional, então sem validação obrigatória
    private String complemento;

    @NotBlank(message = "Cidade não pode ser vazia")
    private String cidade;

    @NotBlank(message = "Estado não pode ser vazio")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ter 2 letras maiúsculas, como SP ou RJ")
    private String estado;

    @NotBlank(message = "CEP não pode ser vazio")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;
}
