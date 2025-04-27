package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.exception.SaldoInsuficienteException;
import br.com.cdb.bancodigital.entity.ContaPoupanca;
import br.com.cdb.bancodigital.repository.ContaCorrenteRepository;
import br.com.cdb.bancodigital.repository.ContaPoupancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessamentoMensalService {

    private final ContaCorrenteRepository correnteRepository;
    private final ContaPoupancaRepository poupancaRepository;

    public void processarTodasContas()
    {
        // Para cada conta corrente, aplica a lógica de taxa mensal (conforme categoria do cliente)
        correnteRepository.findAll().forEach(conta ->
        {
            try
            {
                conta.processarMensalidade();
            } catch (SaldoInsuficienteException e) {
                throw new RuntimeException(e);
            }
        });

        // Para cada conta poupança, aplica o rendimento mensal (conforme categoria do cliente)
        poupancaRepository.findAll().forEach(ContaPoupanca::processarMensalidade);

        // Persiste todas as alterações imediatamente no banco de dados
        correnteRepository.flush();
        poupancaRepository.flush();
    }
}
