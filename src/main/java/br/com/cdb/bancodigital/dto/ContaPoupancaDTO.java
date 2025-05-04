package br.com.cdb.bancodigital.dto;

import lombok.Data;

@Data
public class ContaPoupancaDTO
{
    private Long clienteId; // ID do cliente que será associado à conta
    private String numero;  // Número da conta
}
