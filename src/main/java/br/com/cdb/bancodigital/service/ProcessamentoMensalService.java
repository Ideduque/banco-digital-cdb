package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.repository.ContaCorrenteRepository;
import br.com.cdb.bancodigital.repository.ContaPoupancaRepository;
import br.com.cdb.bancodigital.entity.ContaPoupanca;
import br.com.cdb.bancodigital.entity.ContaCorrente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class ProcessamentoMensalService {

    private final ContaCorrenteRepository correnteRepository;
    private final ContaPoupancaRepository poupancaRepository;

    private final ProcessamentoMensalService service;

    @PostMapping("/mensal")
    public ResponseEntity<String> processarMensal()
    {
        service.processarTodasContas();
        return ResponseEntity.ok("Contas processadas com sucesso.");
    }

    public void processarTodasContas()
    {
        correnteRepository.findAll().forEach(ContaCorrente::processarMensalidade);
        poupancaRepository.findAll().forEach(ContaPoupanca::processarMensalidade);

        correnteRepository.flush();
        poupancaRepository.flush();
    }
}