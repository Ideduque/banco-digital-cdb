package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.service.ProcessamentoMensalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processamento")
@RequiredArgsConstructor
public class ProcessamentoMensalController {

    private final ProcessamentoMensalService service;

    @PostMapping("/mensal")
    public ResponseEntity<String> processarMensal() {
        service.processarTodasContas();
        return ResponseEntity.ok("Contas processadas com sucesso.");
    }
}
