package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.exception.CartaoNaoEncontradoException;
import br.com.cdb.bancodigital.exception.LimiteExcedidoException;
import br.com.cdb.bancodigital.service.CartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController
{
    private final CartaoService cartaoService;

    // Alterar o status do cartão (ativo/desativado)
    @PostMapping("/{cartaoId}/alterar-status")
    public ResponseEntity<String> alterarStatus(@PathVariable String cartaoId, @RequestParam boolean status)
    {
        try
        {
            String mensagem = cartaoService.alterarStatusCartao(cartaoId, status);
            return ResponseEntity.ok(mensagem); // Retorna 200 OK com a mensagem
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 Not Found
        }
    }

    // Alterar a senha do cartão
    @PostMapping("/{cartaoId}/alterar-senha")
    public ResponseEntity<String> alterarSenha(@PathVariable String cartaoId, @RequestParam String novaSenha)
    {
        try
        {
            String mensagem = cartaoService.alterarSenha(cartaoId, novaSenha);
            return ResponseEntity.ok(mensagem); // Retorna 200 OK com a mensagem
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 Not Found
        }
    }

    // Consultar o limite de crédito de um cartão
    @GetMapping("/{cartaoId}/limite-credito")
    public ResponseEntity<BigDecimal> verificarLimiteCredito(@PathVariable String cartaoId)
    {
        try
        {
            BigDecimal limiteCredito = cartaoService.verificarLimiteCredito(cartaoId);
            return ResponseEntity.ok(limiteCredito); // Retorna 200 OK com o limite de crédito
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 Not Found
        }
    }

    // Consultar o limite de débito de um cartão
    @GetMapping("/{cartaoId}/limite-debito")
    public ResponseEntity<BigDecimal> verificarLimiteDebito(@PathVariable String cartaoId)
    {
        try
        {
            BigDecimal limiteDebito = cartaoService.verificarLimiteDebito(cartaoId);
            return ResponseEntity.ok(limiteDebito); // Retorna 200 OK com o limite de débito
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna 404 Not Found
        }
    }

    // Realizar o pagamento no cartão de crédito
    @PostMapping("/{cartaoId}/pagamento-credito")
    public ResponseEntity<String> realizarPagamentoCredito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        try
        {
            String mensagem = cartaoService.realizarPagamentoCredito(cartaoId, valor);
            return ResponseEntity.ok(mensagem); // Retorna 200 OK com a mensagem
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 Not Found
        } catch (LimiteExcedidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna 400 Bad Request
        }
    }

    // Realizar o pagamento no cartão de débito
    @PostMapping("/{cartaoId}/pagamento-debito")
    public ResponseEntity<String> realizarPagamentoDebito(@PathVariable String cartaoId, @RequestParam BigDecimal valor)
    {
        try
        {
            String mensagem = cartaoService.realizarPagamentoDebito(cartaoId, valor);
            return ResponseEntity.ok(mensagem); // Retorna 200 OK com a mensagem
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retorna 404 Not Found
        } catch (LimiteExcedidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna 400 Bad Request
        }
    }
}
