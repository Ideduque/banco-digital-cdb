package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaPoupancaDTO;
import br.com.cdb.bancodigital.entity.ContaPoupanca;
import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.service.ContaPoupancaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/contas/poupanca")
public class ContaPoupancaController {

    @Autowired
    private ContaPoupancaService contaPoupancaService;

    // CREATE: Nova conta poupança
    @PostMapping
    public ResponseEntity<ContaPoupanca> criarConta(@RequestBody ContaPoupancaDTO dto)
    {
        ContaPoupanca conta = contaPoupancaService.criarConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    // READ: Listar todas as contas
    @GetMapping
    public ResponseEntity<List<ContaPoupanca>> listarContas()
    {
        List<ContaPoupanca> contas = contaPoupancaService.listarContas();
        return ResponseEntity.ok(contas);
    }

    // READ: Buscar conta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ContaPoupanca> buscarPorId(@PathVariable Long id)
    {
        ContaPoupanca conta = contaPoupancaService.buscarPorId(id);
        return ResponseEntity.ok(conta);
    }

    // UPDATE: Atualizar conta existente
    @PutMapping("/{id}")
    public ResponseEntity<ContaPoupanca> atualizarConta(
            @PathVariable Long id,
            @RequestBody ContaPoupancaDTO dto)
    {
        ContaPoupanca atualizada = contaPoupancaService.atualizarConta(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    // DELETE: Deletar conta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id)
    {
        contaPoupancaService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }

    // BUSINESS: Aplicar rendimento mensal
    @PostMapping("/{id}/aplicar-rendimento")
    public ResponseEntity<Void> aplicarRendimento(@PathVariable Long id)
    {
        contaPoupancaService.aplicarRendimento(id);
        return ResponseEntity.ok().build();
    }

    // BUSINESS: Realizar saque
    @PostMapping("/{id}/sacar")
    public ResponseEntity<Void> sacar(
            @PathVariable Long id,
            @RequestParam BigDecimal valor)
    {
        try
        {
            contaPoupancaService.sacar(id, valor);
            return ResponseEntity.ok().build();
        } catch (SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}