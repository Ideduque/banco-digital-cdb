package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.CartaoCreditoDTO;
import br.com.cdb.bancodigital.dto.PagamentoCreditoDTO;
import br.com.cdb.bancodigital.entity.Cartao;
import br.com.cdb.bancodigital.exception.*;
import br.com.cdb.bancodigital.entity.CartaoCredito;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.repository.CartaoCreditoRepository;
import br.com.cdb.bancodigital.repository.CartaoRepository;
import br.com.cdb.bancodigital.repository.ContaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartaoCreditoService
{
    private final CartaoCreditoRepository creditoRepository;
    private final ContaRepository contaRepository;
    private final CartaoRepository cartaoRepository;

    // Criação de cartão de crédito
    public CartaoCredito criar(CartaoCreditoDTO dto)
    {
        if (dto.getLimite().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Limite de crédito inválido.");
        }

        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new CartaoNaoEncontradoException("Conta não encontrada"));

        CartaoCredito cartao = CartaoCredito.builder()
                .numero(UUID.randomUUID().toString().substring(0, 8))
                .senha(dto.getSenha())
                .limiteAprovado(dto.getLimite())
                .valorUtilizado(BigDecimal.ZERO)
                .conta(conta)
                .ativo(true)
                .build();

        return creditoRepository.save(cartao);
    }

    // Pagamento usando cartão de crédito
    @Transactional
    public void pagamento(PagamentoCreditoDTO dto)
    {
        CartaoCredito cartao = creditoRepository.findById(dto.getCartaoId())
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        if (!cartao.isAtivo()) {
            throw new CartaoDesativadoException("Cartão desativado");
        }

        if (dto.getSenha() == null || dto.getSenha().isEmpty() || !cartao.getSenha().equals(dto.getSenha())) {
            throw new SenhaInvalidaException("Senha inválida");
        }

        BigDecimal novoValor = cartao.getValorUtilizado().add(dto.getValor());
        if (novoValor.compareTo(cartao.getLimiteAprovado()) > 0) {
            throw new LimiteCreditoExcedidoException("Limite de crédito excedido");
        }

        cartao.setValorUtilizado(novoValor); // atualiza saldo utilizado
        creditoRepository.save(cartao);
    }

    // Método para pagar a fatura de um cartão de crédito
    public String pagarFatura(String cartaoId, BigDecimal valor)
    {
        // Busca o cartão de crédito pelo ID
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado!"));

        if (!(cartao instanceof CartaoCredito cartaoCredito))
        {
            throw new IllegalArgumentException("O cartão informado não é de crédito.");
        }

        // Realiza o pagamento da fatura utilizando o método da classe CartaoCredito
        cartaoCredito.pagarFatura(valor);  // Chama o método pagarFatura da classe CartaoCredito

        cartaoRepository.save(cartaoCredito);  // Atualiza o cartão no banco de dados

        log.info("Fatura paga com sucesso: Cartão ID={} | Valor={}", cartaoId, valor);
        return "Fatura paga com sucesso!";
    }

    @Transactional
    public void trocarStatus(Long id, boolean ativo)
    {
        CartaoCredito cartao = creditoRepository.findById(id)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        cartao.setAtivo(ativo);
        creditoRepository.save(cartao);
    }

    @Transactional
    public void trocarSenha(Long id, String novaSenha)
    {
        CartaoCredito cartao = creditoRepository.findById(id)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        cartao.alterarSenha(novaSenha); // usa método protegido da classe Cartao
        creditoRepository.save(cartao);
    }
}
