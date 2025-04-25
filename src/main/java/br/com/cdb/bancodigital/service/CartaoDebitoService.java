package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.*;
import br.com.cdb.bancodigital.entity.*;
import br.com.cdb.bancodigital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartaoDebitoService {

    private final CartaoDebitoRepository debitoRepo;
    private final ContaRepository contaRepo;

    public CartaoDebito criar(CartaoDebitoDTO dto)
    {
        Conta conta = contaRepo.findById(dto.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        CartaoDebito cartao = CartaoDebito.builder()
                .numero(UUID.randomUUID().toString().substring(0, 8))
                .senha(dto.getSenha())
                .conta(conta)
                .gastoHoje(BigDecimal.ZERO)
                .dataUltimoUso(LocalDate.now().toString())
                .build();

        return debitoRepo.save(cartao);
    }

    public void pagamento(PagamentoDebitoDTO dto) {
        CartaoDebito cartao = debitoRepo.findById(dto.getCartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (!cartao.isAtivo()) throw new RuntimeException("Cartão desativado");
        if (!cartao.getSenha().equals(dto.getSenha())) throw new RuntimeException("Senha inválida");

        LocalDate hoje = LocalDate.now();
        if (!cartao.getDataUltimoUso().equals(hoje.toString()))
        {
            cartao.setGastoHoje(BigDecimal.ZERO);                    //zera limite quando muda o dia
            cartao.setDataUltimoUso(hoje.toString());
        }

        BigDecimal novoGasto = cartao.getGastoHoje().add(dto.getValor());
        if (novoGasto.compareTo(cartao.getLimiteDiario()) > 0)
        {
            throw new RuntimeException("Limite diário excedido");
        }

        Conta conta = cartao.getConta();
        conta.sacar(dto.getValor());

        cartao.setGastoHoje(novoGasto);

        contaRepo.save(conta);
        debitoRepo.save(cartao);
    }

    public void alterarLimite(Long id, BigDecimal novoLimite)
    {
        CartaoDebito cartao = debitoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setLimiteDiario(novoLimite);
        debitoRepo.save(cartao);
    }

    public void trocarStatus(Long id, boolean ativo)
    {
        CartaoDebito cartao = debitoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setAtivo(ativo);
        debitoRepo.save(cartao);
    }

    public void trocarSenha(Long id, String novaSenha)
    {
        CartaoDebito cartao = debitoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        cartao.setSenha(novaSenha);
        debitoRepo.save(cartao);
    }
}

