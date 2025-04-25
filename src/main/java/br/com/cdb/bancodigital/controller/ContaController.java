package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaDTO;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController
{
    private final ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody ContaDTO dto)
    {
        Conta conta = contaService.criarConta(dto);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> exibirsaldo(@PathVariable Long id)
    {
        Conta conta = contaService.buscarPorId(id);
        return ResponseEntity.ok(contaService.exibirSaldo(conta));
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> realizarDeposito(@PathVariable Long id, @RequestParam BigDecimal valor)
    {
        Conta conta = contaService.buscarPorId(id);
        String mensagem = contaService.realizarDeposito(conta, valor);
        return ResponseEntity.ok(mensagem);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<String> realizarSaque(@PathVariable Long id, @RequestParam BigDecimal valor)
    {
        Conta conta = contaService.buscarPorId(id);
        String mensagem = contaService.realizarSaque(conta, valor);
        return ResponseEntity.ok(mensagem);
    }

}

