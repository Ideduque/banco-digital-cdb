package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaCorrenteDTO;
import br.com.cdb.bancodigital.entity.ContaCorrente;
import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.service.ContaCorrenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas/corrente")
public class ContaCorrenteController
{
    @Autowired
    private ContaCorrenteService contaCorrenteService;

    // Cria uma nova conta corrente
    @PostMapping
    public ResponseEntity<ContaCorrente> criarConta(@RequestBody ContaCorrenteDTO dto)
    {
        ContaCorrente conta = contaCorrenteService.criarConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    // Lista todas as contas correntes
    @GetMapping
    public ResponseEntity<List<ContaCorrente>> listarContas()
    {
        List<ContaCorrente> contas = contaCorrenteService.listarContas();
        return ResponseEntity.ok(contas);
    }

    // Busca uma conta corrente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ContaCorrente>> buscarPorId(@PathVariable Long id)
    {
        Optional<ContaCorrente> conta = contaCorrenteService.buscarPorId(id);
        return ResponseEntity.ok(conta);
    }

    // Atualiza uma conta corrente existente
    @PutMapping("/{id}")
    public ResponseEntity<ContaCorrente> atualizarConta(@PathVariable Long id, @RequestBody ContaCorrenteDTO dto)
    {
        ContaCorrente contaAtualizada = contaCorrenteService.atualizarConta(id, dto);
        return ResponseEntity.ok(contaAtualizada);
    }

    // Deleta uma conta corrente pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id)
    {
        contaCorrenteService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }


    // Aplica a mensalidade da conta corrente com base na categoria do cliente
    @PostMapping("/{id}/aplicar-mensalidade")
    public ResponseEntity<Void> aplicarMensalidade(@PathVariable Long id)
    {
        try
        {
            contaCorrenteService.aplicarMensalidade(id);
            return ResponseEntity.ok().build();
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Realiza um saque na conta corrente, considerando o limite disponível
    @PostMapping("/{id}/sacar")
    public ResponseEntity<Void> sacar(@PathVariable Long id, @RequestParam BigDecimal valor)
    {
        try
        {
            contaCorrenteService.sacar(id, valor);
            return ResponseEntity.ok().build();
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}