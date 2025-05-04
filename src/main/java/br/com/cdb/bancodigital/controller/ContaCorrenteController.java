package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaCorrenteDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.ContaCorrente;
import br.com.cdb.bancodigital.enums.TipoConta;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.repository.ContaCorrenteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController // Indica que essa classe trata requisições REST
@RequestMapping("/contas/corrente") // Define a URL base para as operações com conta corrente
public class ContaCorrenteController
{
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository; // Injeta o repositório da conta corrente

    @Autowired
    private ClienteRepository clienteRepository; // Injeta o repositório de clientes

    // Criar nova conta
    @PostMapping
    public ResponseEntity<ContaCorrente> criarConta(@RequestBody ContaCorrenteDTO dto)
    {
        Optional<Cliente> clienteOptional = clienteRepository.findById(dto.getClienteId());

        if (clienteOptional.isEmpty())
        {
            return ResponseEntity.notFound().build(); // Cliente não encontrado
        }

        ContaCorrente conta = new ContaCorrente();
        conta.setCliente(clienteOptional.get());
        conta.setNumero(dto.getNumero());
        conta.setTipoConta(TipoConta.CORRENTE); // Define o tipo como CORRENTE
        conta.setSaldo(BigDecimal.ZERO); // Saldo inicial

        // Chama o método que define o limite com base na categoria do cliente
        conta.definirLimitePorCategoria();

        ContaCorrente salva = contaCorrenteRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // Lista todas contas
    @GetMapping
    public List<ContaCorrente> listarContas()
    {
        return contaCorrenteRepository.findAll();
    }

    // Busca conta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ContaCorrente> buscarPorId(@PathVariable Long id)
    {
        return contaCorrenteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar conta corrente
    @PutMapping("/{id}")
    public ResponseEntity<ContaCorrente> atualizarConta(@PathVariable Long id, @RequestBody ContaCorrenteDTO dto)
    {
        Optional<ContaCorrente> contaOpt = contaCorrenteRepository.findById(id);
        if (contaOpt.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        Optional<Cliente> clienteOptional = clienteRepository.findById(dto.getClienteId());
        if (clienteOptional.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        ContaCorrente conta = contaOpt.get();
        conta.setCliente(clienteOptional.get());
        conta.setNumero(dto.getNumero());

        ContaCorrente atualizada = contaCorrenteRepository.save(conta);
        return ResponseEntity.ok(atualizada);
    }

    // Deleta conta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id)
    {
        if (!contaCorrenteRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }

        contaCorrenteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}