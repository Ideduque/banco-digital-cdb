package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.PixDTO;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.entity.Pix;
import br.com.cdb.bancodigital.repository.ContaRepository;
import br.com.cdb.bancodigital.repository.PixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PixService
{
    private final ContaRepository contaRepository;
    private final PixRepository pixRepository;

    public Pix transferir(PixDTO dto)
    {
        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor da transferência deve ser maior que zero.");
        }

        Conta origem = contaRepository.findById(dto.getContaOrigemId())
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        Conta destino = contaRepository.findById(dto.getContaDestinoId())
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        if (origem.getSaldo().compareTo(dto.getValor()) < 0) {
            throw new RuntimeException("Saldo insuficiente para a transferência.");
        }

        // Realiza a transferência
        origem.sacar(dto.getValor());
        destino.depositar(dto.getValor());

        contaRepository.save(origem);
        contaRepository.save(destino);

        Pix transacao = Pix.builder()
                .contaOrigem(origem)
                .contaDestino(destino)
                .valor(dto.getValor())
                .dataHora(LocalDateTime.now())
                .build();

        return pixRepository.save(transacao);
    }
}
