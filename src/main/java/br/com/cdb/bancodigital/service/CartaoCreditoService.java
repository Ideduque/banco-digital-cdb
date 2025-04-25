package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.*;
import br.com.cdb.bancodigital.entity.*;
import br.com.cdb.bancodigital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository creditoRepository;
    private final ContaRepository contaRepository;

    public CartaoCredito criar(CartaoCreditoDTO dto)
    {
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        CartaoCredito cartao = CartaoCredito.builder()
                .numero(UUID.randomUUID().toString().substring(0, 8))
                .senha(dto.getSenha())
                .limiteAprovado(dto.getLimite())
                .valorUtilizado(BigDecimal.ZERO)
                .conta(conta)
                .build();

        return creditoRepository.save(cartao);
    }

    public void pagamento(PagamentoCreditoDTO dto)
    {
        CartaoCredito cartao = creditoRepository.findById(dto.getCartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (!cartao.isAtivo()) throw new RuntimeException("Cartão desativado");
        if (!cartao.getSenha().equals(dto.getSenha())) throw new RuntimeException("Senha inválida");

        BigDecimal novoValor = cartao.getValorUtilizado().add(dto.getValor());

        if (novoValor.compareTo(cartao.getLimiteAprovado()) > 0) {
            throw new RuntimeException("Limite de crédito excedido");
        }

        cartao.setValorUtilizado(novoValor);
        creditoRepository.save(cartao);
    }

    public void trocarStatus(Long id, boolean ativo)
    {
        CartaoCredito cartao = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setAtivo(ativo);
        creditoRepository.save(cartao);
    }

    public void trocarSenha(Long id, String novaSenha)
    {
        CartaoCredito cartao = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setSenha(novaSenha);
        creditoRepository.save(cartao);
    }
}
