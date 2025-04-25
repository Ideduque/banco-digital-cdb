package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.SeguroDTO;
import br.com.cdb.bancodigital.entity.Seguro;
import br.com.cdb.bancodigital.service.SeguroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seguros")
@RequiredArgsConstructor
public class SeguroController {

    private final SeguroService service;

    // Contrata seguro de viagem para o cartão especificado.
    @PostMapping("/{cartaoId}/viagem")
    public ResponseEntity<String> seguroViagem(@PathVariable String cartaoId) {
        return ResponseEntity.ok(service.contratarSeguroViagem(cartaoId));
    }

    // Contrata seguro de fraude para o cartão especificado.
    @PostMapping("/{cartaoId}/fraude")
    public ResponseEntity<String> seguroFraude(@PathVariable String cartaoId) {
        return ResponseEntity.ok(service.contratarSeguroFraude(cartaoId));
    }
}
