package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.CartaoDTO;
import br.com.cdb.bancodigital.entity.Cartao;
import br.com.cdb.bancodigital.entity.CartaoCredito;
import br.com.cdb.bancodigital.entity.CartaoDebito;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.enums.TipoCartao;
import br.com.cdb.bancodigital.exception.CartaoNaoEncontradoException;
import br.com.cdb.bancodigital.exception.LimiteExcedidoException;
import br.com.cdb.bancodigital.repository.CartaoRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class CartaoService
{
    private final CartaoRepository cartaoRepository;

    // Emite um novo cartão de crédito ou débito
    public CartaoDTO emitirCartao(TipoCartao tipoCartao, Conta conta, String senha, BigDecimal limite) {
        String numero = gerarNumeroCartao(); // Gera número único do cartão
        Cartao novoCartao;

        if (tipoCartao == TipoCartao.CREDITO) {
            novoCartao = new CartaoCredito(); // Cartão de crédito com limite aprovado
        } else if (tipoCartao == TipoCartao.DEBITO) {
            novoCartao = new CartaoDebito(conta, senha, numero, limite); // Cartão de débito com limite diário
        } else {
            throw new IllegalArgumentException("Tipo de cartão inválido!");
        }

        Cartao cartaoSalvo = cartaoRepository.save(novoCartao);
        log.info("Cartão emitido com sucesso: ID={} para Conta={}", cartaoSalvo.getId(), conta.getId());

        return CartaoDTO.fromEntity(cartaoSalvo);
    }

    // Gera um número de cartão com 16 dígitos aleatórios
    private String gerarNumeroCartao() {
        Random random = new Random();
        StringBuilder numeroCartao = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numeroCartao.append(random.nextInt(10));
        }
        return numeroCartao.toString();
    }

    // Altera o status do cartão (ativo/inativo)
    public CartaoDTO alterarStatusCartao(String cartaoId, boolean status) {
        Cartao cartao = buscarCartaoPorId(cartaoId);
        cartao.setAtivo(status);
        Cartao cartaoAtualizado = cartaoRepository.save(cartao);

        log.info("Status do cartão alterado: ID={} | Novo status={}", cartaoId, status);
        return converterParaDTO(cartaoAtualizado);
    }

    // Altera a senha do cartão
    public String alterarSenha(String cartaoId, String novaSenha) {
        Cartao cartao = buscarCartaoPorId(cartaoId);
        cartao.alterarSenha(novaSenha); // Método definido na superclasse Cartao

        cartaoRepository.save(cartao);
        log.info("Senha alterada para cartão: ID={}", cartaoId);

        return "Senha alterada com sucesso!";
    }

    // Verifica o limite de crédito disponível, se o cartão for de crédito
    public BigDecimal verificarLimiteCredito(String cartaoId) {
        Cartao cartao = buscarCartaoPorId(cartaoId);
        if (cartao instanceof CartaoCredito credito) {
            return credito.getLimiteAprovado();
        }
        return BigDecimal.ZERO;
    }

    // Verifica o limite diário disponível, se o cartão for de débito
    public BigDecimal verificarLimiteDebito(String cartaoId) {
        Cartao cartao = buscarCartaoPorId(cartaoId);
        if (cartao instanceof CartaoDebito debito) {
            return debito.getLimiteDiario();
        }
        return BigDecimal.ZERO;
    }

    // Realiza um pagamento com cartão de crédito
    public String realizarPagamentoCredito(String cartaoId, BigDecimal valor) {
        Cartao cartao = buscarCartaoPorId(cartaoId);

        if (!(cartao instanceof CartaoCredito credito)) {
            throw new CartaoNaoEncontradoException("Cartão de crédito não encontrado!");
        }

        if (credito.getValorUtilizado().add(valor).compareTo(credito.getLimiteAprovado()) > 0) {
            throw new LimiteExcedidoException("Limite de crédito excedido!");
        }

        credito.setValorUtilizado(credito.getValorUtilizado().add(valor));
        cartaoRepository.save(credito);

        log.info("Pagamento de crédito realizado: ID={} | Valor={}", cartaoId, valor);
        return "Pagamento de crédito realizado com sucesso!";
    }

    // Realiza um pagamento com cartão de débito
    public String realizarPagamentoDebito(String cartaoId, BigDecimal valor) {

        Cartao cartao = buscarCartaoPorId(cartaoId);

        if (!(cartao instanceof CartaoDebito debito)) {
            throw new CartaoNaoEncontradoException("Cartão de débito não encontrado!");
        }

        if (debito.getSaldoUtilizado().add(valor).compareTo(debito.getLimiteDiario()) > 0) {
            throw new LimiteExcedidoException("Limite diário excedido!");
        }

        debito.setSaldoUtilizado(debito.getSaldoUtilizado().add(valor));
        cartaoRepository.save(debito);

        log.info("Pagamento de débito realizado: ID={} | Valor={}", cartaoId, valor);
        return "Pagamento de débito realizado com sucesso!";
    }

    // Recupera um cartão por ID ou lança exceção
    private Cartao buscarCartaoPorId(String cartaoId) {
        return cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado!"));
    }

    // Converte a entidade Cartao para DTO
    private CartaoDTO converterParaDTO(Cartao cartao)
    {
        return  CartaoDTO.fromEntity(cartao);
    }
}
