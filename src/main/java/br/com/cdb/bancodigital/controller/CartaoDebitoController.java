package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.*;
import br.com.cdb.bancodigital.entity.CartaoDebito;
import br.com.cdb.bancodigital.service.CartaoDebitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cartoes/debito")
@RequiredArgsConstructor
public class CartaoDebitoController
{
    private final CartaoDebitoService service;

    @PostMapping
    public ResponseEntity<CartaoDebito> criar(@RequestBody CartaoDebitoDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PostMapping("/pagamento")
    public ResponseEntity<String> pagar(@RequestBody PagamentoDebitoDTO dto)
    {
        service.pagamento(dto);
        return ResponseEntity.ok("Pagamento realizado com sucesso");
    }

    @PatchMapping("/{id}/limite")
    public ResponseEntity<String> alterarLimite(@PathVariable Long id, @RequestBody LimiteDebitoDTO dto)
    {
        service.alterarLimite(id, dto.getNovoLimite());
        return ResponseEntity.ok("Limite alterado com sucesso");
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

