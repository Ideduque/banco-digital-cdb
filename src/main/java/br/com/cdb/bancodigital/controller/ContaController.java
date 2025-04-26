package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ContaDTO;
import br.com.cdb.bancodigital.dto.TransferenciaDTO;
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
    // Injeção automática do serviço de contas via Lombok
    private final ContaService contaService;

    // Endpoint para criar uma nova conta (corrente ou poupança)
    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody ContaDTO dto)
    {
        Conta conta = contaService.criarConta(dto);
        return ResponseEntity.ok(conta);
    }

    // Endpoint para exibir o saldo da conta
    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> exibirsaldo(@PathVariable Long id)
    {
        Conta conta = contaService.buscarPorId(id);
        return ResponseEntity.ok(contaService.exibirSaldo(conta));
    }

    // Endpoint para realizar depósito
    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> realizarDeposito(@PathVariable Long id, @RequestParam BigDecimal valor)
    {
        Conta conta = contaService.buscarPorId(id);
        String mensagem = contaService.realizarDeposito(conta, valor);
        return ResponseEntity.ok(mensagem);
    }

    // Endpoint para realizar saque
    @PostMapping("/{id}/saque")
    public ResponseEntity<String> realizarSaque(@PathVariable Long id, @RequestParam BigDecimal valor)
    {
        Conta conta = contaService.buscarPorId(id);
        String mensagem = contaService.realizarSaque(conta, valor);
        return ResponseEntity.ok(mensagem);
    }

    // Endpoint para realizar transferência entre duas contas
    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferir(@RequestBody TransferenciaDTO dto)
    {
        // Busca as contas
        var origem = contaService.buscarPorId(dto.getContaOrigemId());
        var destino = contaService.buscarPorId(dto.getContaDestinoId());

        // Chama o método de serviço
        String mensagem = contaService.realizarTransferencia(origem, destino, dto.getValor());

        return ResponseEntity.ok(mensagem);
    }

}

