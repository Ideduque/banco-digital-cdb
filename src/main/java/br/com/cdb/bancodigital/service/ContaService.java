package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.ContaDTO;
import br.com.cdb.bancodigital.entity.*;
import br.com.cdb.bancodigital.enums.TipoTransacao;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.repository.ContaRepository;
import br.com.cdb.bancodigital.repository.ExtratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final ExtratoRepository extratoRepository;

    // Criar conta
    public Conta criarConta(ContaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String numeroConta = UUID.randomUUID().toString().substring(0, 8);

        Conta conta;
        if (dto.getTipo().equalsIgnoreCase("CORRENTE")) {
            conta = new ContaCorrente();
        } else if (dto.getTipo().equalsIgnoreCase("POUPANCA")) {
            conta = new ContaPoupanca();
        } else {
            throw new RuntimeException("Tipo de conta inválido");
        }

        conta.setNumero(numeroConta);
        conta.setCliente(cliente);
        return contaRepository.save(conta);
    }

    // Buscar conta
    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // Exibir saldo
    public BigDecimal exibirSaldo(Conta conta) {
        return conta.getSaldo();
    }

    // Realizar depósito
    public String realizarDeposito(Conta conta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        conta.setSaldo(conta.getSaldo().add(valor));

        Extrato extrato = new Extrato(null, LocalDateTime.now(), "Depósito realizado", valor, conta, TipoTransacao.DEPOSITO);
        extratoRepository.save(extrato);

        return "Depósito realizado com sucesso!";
    }

    // Realizar saque
    public String realizarSaque(Conta conta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));

        Extrato extrato = new Extrato(null, LocalDateTime.now(), "Saque realizado", valor, conta, TipoTransacao.SAQUE);
        extratoRepository.save(extrato);

        return "Saque realizado com sucesso!";
    }
}
