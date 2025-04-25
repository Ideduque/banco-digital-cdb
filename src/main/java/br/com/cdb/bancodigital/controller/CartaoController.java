package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.service.CartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping("/{cartaoId}/alterar-status")
    public String alterarStatus(@PathVariable String cartaoId, @RequestParam boolean status)
    {
        return cartaoService.alterarStatusCartao(cartaoId, status);
    }

    @PostMapping("/{cartaoId}/alterar-senha")
    public String alterarSenha(@PathVariable String cartaoId, @RequestParam String novaSenha)
    {
        return cartaoService.alterarSenha(cartaoId, novaSenha);
    }

    @GetMapping("/{cartaoId}/limite-credito")
    public BigDecimal verificarLimiteCredito(@PathVariable String cartaoId)
    {
        return cartaoService.verificarLimiteCredito(cartaoId);
    }

    @GetMapping("/{cartaoId}/limite-debito")
    public BigDecimal verificarLimiteDebito(@PathVariable String cartaoId)
    {
        return cartaoService.verificarLimiteDebito(cartaoId);
    }

    @PostMapping("/{cartaoId}/pagamento-credito")
    public String realizarPagamentoCredito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        return cartaoService.realizarPagamentoCredito(cartaoId, valor);
    }

    @PostMapping("/{cartaoId}/pagamento-debito")
    public String realizarPagamentoDebito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        return cartaoService.realizarPagamentoDebito(cartaoId, valor);
    }
}
