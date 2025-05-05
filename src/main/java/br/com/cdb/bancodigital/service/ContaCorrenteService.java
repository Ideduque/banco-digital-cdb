package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.ContaCorrenteDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.ContaCorrente;
import br.com.cdb.bancodigital.enums.TipoConta;
import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.repository.ContaCorrenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContaCorrenteService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

     //Cria uma nova conta corrente para um cliente existente.
    public ContaCorrente criarConta(ContaCorrenteDTO dto)
    {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        ContaCorrente conta = new ContaCorrente();
        conta.setCliente(cliente);
        conta.setNumero(dto.getNumero());
        conta.setTipoConta(TipoConta.CORRENTE);
        conta.setSaldo(BigDecimal.ZERO);

        // Define o limite com base na categoria do cliente
        conta.definirLimitePorCategoria();

        return contaCorrenteRepository.save(conta);
    }

     //Lista todas as contas correntes.
    public List<ContaCorrente> listarContas()
    {
        return contaCorrenteRepository.findAll();
    }

     //Busca uma conta corrente pelo ID.
    public Optional<ContaCorrente> buscarPorId(Long id)
    {
        return contaCorrenteRepository.findById(id);
    }

    //Atualiza os dados de uma conta corrente existente.
    public ContaCorrente atualizarConta(Long id, ContaCorrenteDTO dto)
    {
        ContaCorrente conta = contaCorrenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        conta.setCliente(cliente);
        conta.setNumero(dto.getNumero());

        return contaCorrenteRepository.save(conta);
    }

    //Deleta uma conta corrente pelo ID.
    public void deletarConta(Long id)
    {
        if (!contaCorrenteRepository.existsById(id))
        {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        contaCorrenteRepository.deleteById(id);
    }

     // Aplica a mensalidade da conta corrente com base na categoria do cliente.
    public void aplicarMensalidade(Long contaId) throws SaldoInsuficienteException
    {
        ContaCorrente conta = contaCorrenteRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta corrente não encontrada"));

        conta.processarMensalidade();
        contaCorrenteRepository.save(conta);
    }

    //Realiza um saque na conta corrente, considerando o limite disponível.
    public void sacar(Long contaId, BigDecimal valor) throws SaldoInsuficienteException
    {
        ContaCorrente conta = contaCorrenteRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta corrente não encontrada"));

        BigDecimal saldoDisponivel = conta.getSaldo().add(conta.getLimite());

        if (valor.compareTo(saldoDisponivel) > 0)
        {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaCorrenteRepository.save(conta);
    }
}