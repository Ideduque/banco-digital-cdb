package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.CartaoDTO;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.enums.TipoCartao;
import br.com.cdb.bancodigital.service.CartaoService;
import br.com.cdb.bancodigital.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController
{
    private final CartaoService cartaoService;
    private final ContaService contaService;

    // Emissão de novo cartão
    @PostMapping("/emitir")
    public CartaoDTO emitirCartao(
            @RequestParam TipoCartao tipoCartao,
            @RequestParam String contaId,
            @RequestParam String senha,
            @RequestParam BigDecimal limite)
    {
        Conta conta = contaService.buscarPorId(Long.valueOf(contaId));
        return cartaoService.emitirCartao(tipoCartao, conta, senha, limite);
    }

    // Alterar status (ativo ou inativo) do cartão
    @PostMapping("/{cartaoId}/alterar-status")
    public ResponseEntity<String> alterarStatus(@PathVariable String cartaoId, @RequestParam boolean status)
    {
        String cartaoDTO = String.valueOf(cartaoService.alterarStatusCartao(cartaoId, status));
        return ResponseEntity.ok(cartaoDTO);
    }

    // Alterar a senha do cartão
    @PostMapping("/{cartaoId}/alterar-senha")
    public ResponseEntity<String> alterarSenha(@PathVariable String cartaoId, @RequestParam String novaSenha)
    {
        if (novaSenha == null || novaSenha.isBlank())
        {
            return ResponseEntity.badRequest().body("Nova senha não pode ser vazia.");
        }
        String mensagem = cartaoService.alterarSenha(cartaoId, novaSenha);
        return ResponseEntity.ok(mensagem);
    }

    // Consultar o limite de crédito disponível
    @GetMapping("/{cartaoId}/limite-credito")
    public ResponseEntity<BigDecimal> verificarLimiteCredito(@PathVariable String cartaoId)
    {
        BigDecimal limite = cartaoService.verificarLimiteCredito(cartaoId);
        return ResponseEntity.ok(limite);
    }

    // Consultar o limite de débito diário
    @GetMapping("/{cartaoId}/limite-debito")
    public ResponseEntity<BigDecimal> verificarLimiteDebito(@PathVariable String cartaoId)
    {
        BigDecimal limite = cartaoService.verificarLimiteDebito(cartaoId);
        return ResponseEntity.ok(limite);
    }

    // Realizar pagamento utilizando o crédito
    @PostMapping("/{cartaoId}/pagamento-credito")
    public ResponseEntity<String> realizarPagamentoCredito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            return ResponseEntity.badRequest().body("O valor do pagamento deve ser maior que zero.");
        }
        String mensagem = cartaoService.realizarPagamentoCredito(cartaoId, valor);
        return ResponseEntity.ok(mensagem);
    }

    // Realizar pagamento utilizando o débito
    @PostMapping("/{cartaoId}/pagamento-debito")
    public ResponseEntity<String> realizarPagamentoDebito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
        {
            return ResponseEntity.badRequest().body("O valor do pagamento deve ser maior que zero.");
        }
        String mensagem = cartaoService.realizarPagamentoDebito(cartaoId, valor);
        return ResponseEntity.ok(mensagem);
    }
}