package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.entity.CartaoCredito;
import br.com.cdb.bancodigital.entity.Seguro;
import br.com.cdb.bancodigital.enums.TipoSeguro;
import br.com.cdb.bancodigital.repository.SeguroRepository;
import br.com.cdb.bancodigital.repository.CartaoCreditoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeguroService
{
    private final SeguroRepository seguroRepository;
    private final CartaoCreditoRepository creditoRepository;

    public String contratarSeguroViagem(String cartaoCreditoId)
    {
        CartaoCredito cartao = creditoRepository.findById(Long.valueOf(cartaoCreditoId))
                .orElseThrow(() -> new EntityNotFoundException("Cartão de crédito não encontrado"));

        BigDecimal valor = BigDecimal.valueOf(50);

        Seguro seguro = Seguro.builder()
                .numeroApolice(UUID.randomUUID().toString().substring(0, 8))
                .dataContratacao(LocalDate.now())
                .valor(valor)
                .condicoes("Seguro de viagem")
                .cartaoCredito(cartao)
                .tipoSeguro(TipoSeguro.VIAGEM)
                .build();

        seguroRepository.save(seguro);
        return String.format("Seguro de viagem contratado com sucesso! Valor: R$ %s", valor);
    }

    public String contratarSeguroFraude(String cartaoCreditoId)
    {
        CartaoCredito cartao = creditoRepository.findById(Long.valueOf(cartaoCreditoId))
                .orElseThrow(() -> new EntityNotFoundException("Cartão de crédito não encontrado"));

        Seguro seguro = Seguro.builder()
                .numeroApolice(UUID.randomUUID().toString().substring(0, 8))
                .dataContratacao(LocalDate.now())
                .valor(BigDecimal.valueOf(5000))
                .condicoes("Seguro de fraude")
                .cartaoCredito(cartao)
                .tipoSeguro(TipoSeguro.FRAUDE)
                .build();

        seguroRepository.save(seguro);
        return "Seguro de fraude contratado com sucesso, valor de apólice de R$ 5000.00.";
    }
}

