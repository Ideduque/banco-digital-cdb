package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.entity.Cartao;
import br.com.cdb.bancodigital.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    // Ativar/desativar cartão
    public String alterarStatusCartao(String cartaoId, boolean status) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent()) {
            Cartao cartao = cartaoOpt.get();
            cartao.setAtivo(status);
            cartaoRepository.save(cartao);
            return status ? "Cartão ativado com sucesso!" : "Cartão desativado com sucesso!";
        }
        return "Cartão não encontrado!";
    }

    public String alterarSenha(String cartaoId, String novaSenha) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent()) {
            Cartao cartao = cartaoOpt.get();
            cartao.alterarSenha(novaSenha);
            cartaoRepository.save(cartao);
            return "Senha alterada com sucesso!";
        }
        return "Cartão não encontrado!";
    }

    public BigDecimal verificarLimiteCredito(String cartaoId) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && "CREDITO".equalsIgnoreCase(cartaoOpt.get().getTipo())) {
            return cartaoOpt.get().getLimiteCredito();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal verificarLimiteDebito(String cartaoId) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && "DEBITO".equalsIgnoreCase(cartaoOpt.get().getTipo())) {
            return cartaoOpt.get().getLimiteDiario();
        }
        return BigDecimal.ZERO;
    }

    public String realizarPagamentoCredito(String cartaoId, BigDecimal valor) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && "CREDITO".equalsIgnoreCase(cartaoOpt.get().getTipo())) {
            Cartao cartao = cartaoOpt.get();
            BigDecimal saldoAtual = cartao.getSaldoUtilizado();
            BigDecimal limite = cartao.getLimiteCredito();

            if (saldoAtual.add(valor).compareTo(limite) <= 0) {
                cartao.setSaldoUtilizado(saldoAtual.add(valor));
                cartaoRepository.save(cartao);
                return "Pagamento realizado com sucesso!";
            } else {
                return "Limite de crédito excedido!";
            }
        }
        return "Cartão de crédito não encontrado!";
    }

    public String realizarPagamentoDebito(String cartaoId, BigDecimal valor) {
        Optional<Cartao> cartaoOpt = cartaoRepository.findById(cartaoId);
        if (cartaoOpt.isPresent() && "DEBITO".equalsIgnoreCase(cartaoOpt.get().getTipo())) {
            Cartao cartao = cartaoOpt.get();
            BigDecimal limiteDiario = cartao.getLimiteDiario();
            BigDecimal saldoAtual = cartao.getSaldoUtilizado();

            if (valor.add(saldoAtual).compareTo(limiteDiario) <= 0) {
                cartao.setSaldoUtilizado(saldoAtual.add(valor));
                cartaoRepository.save(cartao);
                return "Pagamento realizado com sucesso!";
            } else {
                return "Limite diário excedido!";
            }
        }
        return "Cartão de débito não encontrado!";
    }
}
