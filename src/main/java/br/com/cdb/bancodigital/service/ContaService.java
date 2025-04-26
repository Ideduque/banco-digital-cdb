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

@Service // Marca a classe como um componente de serviço do Spring
@RequiredArgsConstructor // Lombok: gera construtor com os campos final (injeção de dependências)
public class ContaService 
{
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;
    private final ExtratoRepository extratoRepository;

    // Criar conta a partir de um DTO
    public Conta criarConta(ContaDTO dto)
    {
        // Busca o cliente pelo ID informado no DTO
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Gera um número de conta aleatório (8 caracteres)
        String numeroConta = UUID.randomUUID().toString().substring(0, 8);

        Conta conta;
        // Instancia o tipo correto de conta com base no tipo informado no DTO
        if (dto.getTipo().equalsIgnoreCase("CORRENTE"))
        {
            conta = new ContaCorrente();
        } else if (dto.getTipo().equalsIgnoreCase("POUPANCA"))
        {
            conta = new ContaPoupanca();
        } else {
            throw new RuntimeException("Tipo de conta inválido");
        }

        // Atribui os dados à conta criada
        conta.setNumero(numeroConta);
        conta.setCliente(cliente);

        // Salva e retorna a conta criada
        return contaRepository.save(conta);
    }

    // Buscar conta por ID
    public Conta buscarPorId(Long id)
    {
        return contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // Exibir saldo da conta
    public BigDecimal exibirSaldo(Conta conta)
    {
        return conta.getSaldo();
    }

    // Realizar depósito
    public String realizarDeposito(Conta conta, BigDecimal valor)
    {
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        // Adiciona o valor ao saldo
        conta.setSaldo(conta.getSaldo().add(valor));

        // Salva movimentação no extrato
        Extrato extrato = new Extrato(
                null,
                LocalDateTime.now(),
                "Depósito realizado",
                valor,
                conta,
                TipoTransacao.DEPOSITO);
        extratoRepository.save(extrato);

        return "Depósito realizado com sucesso!";
    }

    // Realizar saque
    public String realizarSaque(Conta conta, BigDecimal valor)
    {
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }

        if (conta.getSaldo().compareTo(valor) < 0)
        {
            throw new IllegalArgumentException("Saldo insuficiente para o saque.");
        }

        // Subtrai o valor do saldo
        conta.setSaldo(conta.getSaldo().subtract(valor));

        // Salva movimentação no extrato
        Extrato extrato = new Extrato(
                null,
                LocalDateTime.now(),
                "Saque realizado",
                valor,
                conta,
                TipoTransacao.SAQUE);
        extratoRepository.save(extrato);

        return "Saque realizado com sucesso!";
    }

    public String realizarTransferencia(Conta origem, Conta destino, BigDecimal valor)
    {
        // Validação do valor
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

        // Verifica se a conta de origem tem saldo suficiente
        if (origem.getSaldo().compareTo(valor) < 0)
        {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transferência.");
        }

        // Realiza a transferência
        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        // Registra o extrato da conta de origem
        Extrato extratoOrigem = new Extrato(
                null,
                LocalDateTime.now(),
                "Transferência enviada para conta " + destino.getNumero(),
                valor,
                origem,
                TipoTransacao.TRANSFERENCIA
        );

        // Registra o extrato da conta de destino
        Extrato extratoDestino = new Extrato(
                null,
                LocalDateTime.now(),
                "Transferência recebida da conta " + origem.getNumero(),
                valor,
                destino,
                TipoTransacao.TRANSFERENCIA
        );

        // Salva extratos
        extratoRepository.save(extratoOrigem);
        extratoRepository.save(extratoDestino);

        // Salva as alterações nas contas
        contaRepository.save(origem);
        contaRepository.save(destino);

        return "Transferência realizada com sucesso!";
    }
}