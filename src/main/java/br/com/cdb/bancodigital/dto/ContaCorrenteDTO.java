package br.com.cdb.bancodigital.dto;

import lombok.Data;

@Data
public class ContaCorrenteDTO
{
    private Long clienteId; // ID do cliente associado
    private String numero;  // Número da conta
}
