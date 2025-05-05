package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.ContaPoupancaDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.ContaPoupanca;
import br.com.cdb.bancodigital.enums.TipoConta;
import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.repository.ContaPoupancaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaPoupancaService
{
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

     //Cria uma nova conta poupança com base nos dados fornecidos.
    @Transactional
    public ContaPoupanca criarConta(ContaPoupancaDTO dto)
    {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        ContaPoupanca conta = new ContaPoupanca();
        conta.setCliente(cliente);
        conta.setNumero(dto.getNumero());
        conta.setTipoConta(TipoConta.POUPANCA);
        conta.setSaldo(BigDecimal.ZERO);

        return contaPoupancaRepository.save(conta);
    }

     //Lista todas as contas poupança.
    @Transactional(readOnly = true)
    public List<ContaPoupanca> listarContas()
    {
        return contaPoupancaRepository.findAll();
    }

     //Busca uma conta poupança pelo ID.
    @Transactional(readOnly = true)
    public ContaPoupanca buscarPorId(Long id)
    {
        return contaPoupancaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta poupança não encontrada"));
    }

     // Atualiza uma conta poupança existente com base nos dados fornecidos.
    @Transactional
    public ContaPoupanca atualizarConta(Long id, ContaPoupancaDTO dto)
    {
        ContaPoupanca conta = contaPoupancaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta poupança não encontrada"));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        conta.setCliente(cliente);
        conta.setNumero(dto.getNumero());

        return contaPoupancaRepository.save(conta);
    }

     // Deleta uma conta poupança pelo ID.
    @Transactional
    public void deletarConta(Long id) {
        if (!contaPoupancaRepository.existsById(id))
        {
            throw new IllegalArgumentException("Conta poupança não encontrada");
        }
        contaPoupancaRepository.deleteById(id);
    }

     //Aplica o rendimento mensal à conta poupança com base na categoria do cliente.
    @Transactional
    public void aplicarRendimento(Long contaId)
    {
        ContaPoupanca conta = contaPoupancaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta poupança não encontrada"));

        conta.processarMensalidade();
        contaPoupancaRepository.save(conta);
    }

     // Realiza um saque na conta poupança, garantindo que o saldo seja suficiente.
    @Transactional
    public void sacar(Long contaId, BigDecimal valor) throws SaldoInsuficienteException
    {
        ContaPoupanca conta = contaPoupancaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta poupança não encontrada"));

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaPoupancaRepository.save(conta);
    }
}
