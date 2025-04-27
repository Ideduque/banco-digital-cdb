package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.entity.Cartao;
import br.com.cdb.bancodigital.enums.TipoCartao;
import br.com.cdb.bancodigital.repository.CartaoRepository;
import br.com.cdb.bancodigital.exception.CartaoNaoEncontradoException;
import br.com.cdb.bancodigital.exception.LimiteExcedidoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartaoService
{
    private final CartaoRepository cartaoRepository;

    // Ativar/desativar cartão
    public String alterarStatusCartao(String cartaoId, boolean status)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent())
        {
            Cartao cartao = cartaoOpt.get();
            cartao.setAtivo(status);
            cartaoRepository.save(cartao);
            return status ? "Cartão ativado com sucesso!" : "Cartão desativado com sucesso!";
        }
        throw new CartaoNaoEncontradoException("Cartão não encontrado!");
    }

    // Alterar senha do cartão
    public String alterarSenha(String cartaoId, String novaSenha)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent())
        {
            Cartao cartao = cartaoOpt.get();
            cartao.alterarSenha(novaSenha);
            cartaoRepository.save(cartao);
            return "Senha alterada com sucesso!";
        }
        throw new CartaoNaoEncontradoException("Cartão não encontrado!");
    }

    // Verificar limite de crédito
    public BigDecimal verificarLimiteCredito(String cartaoId)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && TipoCartao.CREDITO.equals(cartaoOpt.get().getTipoCartao()))
        {
            return cartaoOpt.get().getLimiteCredito();
        }
        return BigDecimal.ZERO;
    }

    // Verificar limite de débito
    public BigDecimal verificarLimiteDebito(String cartaoId)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && TipoCartao.DEBITO.equals(cartaoOpt.get().getTipoCartao()))
        {
            return cartaoOpt.get().getLimiteDiario();
        }
        return BigDecimal.ZERO;
    }

    // Realizar pagamento no cartão de crédito
    public String realizarPagamentoCredito(String cartaoId, BigDecimal valor)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && TipoCartao.CREDITO.equals(cartaoOpt.get().getTipoCartao()))
        {
            Cartao cartao = cartaoOpt.get();
            BigDecimal saldoAtual = cartao.getSaldoUtilizado();
            BigDecimal limite = cartao.getLimiteCredito();

            if (saldoAtual.add(valor).compareTo(limite) > 0)
            {
                throw new LimiteExcedidoException("Limite de crédito excedido!");
            }

            cartao.setSaldoUtilizado(saldoAtual.add(valor));
            cartaoRepository.save(cartao);
            return "Pagamento realizado com sucesso!";
        }
        throw new CartaoNaoEncontradoException("Cartão de crédito não encontrado!");
    }

    // Realizar pagamento no cartão de débito
    public String realizarPagamentoDebito(String cartaoId, BigDecimal valor)
    {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && TipoCartao.DEBITO.equals(cartaoOpt.get().getTipoCartao()))
        {
            Cartao cartao = cartaoOpt.get();
            BigDecimal limiteDiario = cartao.getLimiteDiario();
            BigDecimal saldoAtual = cartao.getSaldoUtilizado();

            if (valor.add(saldoAtual).compareTo(limiteDiario) > 0)
            {
                throw new LimiteExcedidoException("Limite diário excedido!");
            }

            cartao.setSaldoUtilizado(saldoAtual.add(valor));
            cartaoRepository.save(cartao);
            return "Pagamento realizado com sucesso!";
        }
        throw new CartaoNaoEncontradoException("Cartão de débito não encontrado!");
    }
}
