package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaPoupancaDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.ContaPoupanca;
import br.com.cdb.bancodigital.enums.TipoConta;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.repository.ContaPoupancaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/contas/poupanca") // Mapeia as requisições para o endpoint base /contas/poupanca
public class ContaPoupancaController
{
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository; // Injeta o repositório da conta poupança

    @Autowired
    private ClienteRepository clienteRepository; // Injeta o repositório de clientes

    // Criar nova conta
    @PostMapping
    public ResponseEntity<ContaPoupanca> criarConta (@RequestBody ContaPoupancaDTO dto)
    {
        // Busca o cliente pelo ID informado no DTO
        Optional<Cliente> clienteOpt = clienteRepository.findById(dto.getClienteId());
        if (clienteOpt.isEmpty())
        {
            return ResponseEntity.notFound().build(); // Se não encontrar o cliente, retorna 404
        }

        // Cria a nova conta poupança
        ContaPoupanca conta = new ContaPoupanca();
        conta.setCliente(clienteOpt.get());
        conta.setNumero(dto.getNumero());
        conta.setTipoConta(TipoConta.POUPANCA); // Define o tipo como POUPANÇA
        conta.setSaldo(BigDecimal.ZERO); // Saldo inicial zero

        // Salva a conta no banco
        ContaPoupanca salva = contaPoupancaRepository.save(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva); // Retorna 201 com o objeto salvo
    }

    // Lista todas as contas
    @GetMapping
    public List<ContaPoupanca> listarContas()
    {
        return contaPoupancaRepository.findAll(); // Retorna todas as contas poupança do banco
    }

    // Busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<ContaPoupanca> buscarPorId (@PathVariable Long id)
    {
        // Busca a conta pelo ID
        return contaPoupancaRepository.findById(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a conta
                .orElse(ResponseEntity.notFound().build()); // Se não encontrar, retorna 404
    }

    // Atualiza conta existente
    @PutMapping("/{id}")
    public ResponseEntity<ContaPoupanca> atualizarConta (@PathVariable Long id, @RequestBody ContaPoupancaDTO dto)
    {
        // Verifica se a conta existe
        Optional<ContaPoupanca> contaOptional = contaPoupancaRepository.findById(id);
        if (contaOptional.isEmpty())
        {
            return ResponseEntity.notFound().build(); // Se não existir, retorna 404
        }

        ContaPoupanca conta = contaOptional.get();

        // Verifica se o cliente informado no DTO existe
        Optional<Cliente> clienteOptional = clienteRepository.findById(dto.getClienteId());
        if (clienteOptional.isEmpty())
        {
            return ResponseEntity.badRequest().build(); // Cliente não encontrado = 400 Bad Request
        }

        // Atualiza os dados da conta
        conta.setCliente(clienteOptional.get());
        conta.setNumero(dto.getNumero());

        // Salva as alterações
        ContaPoupanca atualizada = contaPoupancaRepository.save(conta);
        return ResponseEntity.ok(atualizada); // Retorna 200 OK com a conta atualizada
    }

    // Deleta conta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id)
    {
        // Verifica se a conta existe
        if (!contaPoupancaRepository.existsById(id))
        {
            return ResponseEntity.notFound().build(); // Se não existir, retorna 404
        }

        // Remove a conta do banco
        contaPoupancaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (sem corpo)
    }
}
