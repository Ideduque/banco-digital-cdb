package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.*;
import br.com.cdb.bancodigital.entity.CartaoCredito;
import br.com.cdb.bancodigital.service.CartaoCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes/credito")
@RequiredArgsConstructor
public class CartaoCreditoController {

    private final CartaoCreditoService service;

    @PostMapping
    public ResponseEntity<CartaoCredito> criar(@RequestBody CartaoCreditoDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PostMapping("/pagamento")
    public ResponseEntity<String> pagar(@RequestBody PagamentoCreditoDTO dto)
    {
        service.pagamento(dto);
        return ResponseEntity.ok("Pagamento realizado com sucesso");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> status(@PathVariable Long id, @RequestParam boolean ativo)
    {
        service.trocarStatus(id, ativo);
        return ResponseEntity.ok("Status atualizado");
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<String> senha(@PathVariable Long id, @RequestParam String novaSenha)
    {
        service.trocarSenha(id, novaSenha);
        return ResponseEntity.ok("Senha alterada com sucesso");
    }
}

