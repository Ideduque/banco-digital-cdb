package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.CartaoDebitoDTO;
import br.com.cdb.bancodigital.dto.PagamentoDebitoDTO;
import br.com.cdb.bancodigital.entity.CartaoDebito;
import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.repository.CartaoDebitoRepository;
import br.com.cdb.bancodigital.repository.ContaRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Data
public class CartaoDebitoService
{
    // Repositórios necessários para acessar as entidades de Cartão de Débito e Conta
    private final CartaoDebitoRepository debitoRepository;
    private final ContaRepository contaRepository;

     //Método para criar um novo Cartão de Débito.
     //Recebe um DTO (Data Transfer Object) com os dados necessários e cria o cartão.
    public CartaoDebito criar(CartaoDebitoDTO dto)
    {
        // Buscar a conta associada ao cartão de débito pelo ID
        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        // Criar o Cartão de Débito
        CartaoDebito cartao = new CartaoDebito(
                conta, // A conta associada ao cartão
                dto.getSenha(), // Senha do cartão
                UUID.randomUUID().toString().substring(0, 8), // Gerar número único para o cartão (8 caracteres)
                dto.getLimiteDiario() // Limite diário passado no DTO
        );

        // Definir o tipo do cartão como "DEBITO"
        cartao.setTipoCartao(br.com.cdb.bancodigital.enums.TipoCartao.DEBITO);

        // Salvar o Cartão de Débito no banco de dados e retornar
        return debitoRepository.save(cartao);
    }

    /**
     * Método para realizar um pagamento com o Cartão de Débito.
     * O pagamento verifica se o cartão está ativo, se a senha está correta,
     * e se o limite diário não foi excedido.
     */
    public void pagamento(PagamentoDebitoDTO dto)
    {
        // Buscar o Cartão de Débito pelo ID
        CartaoDebito cartao = debitoRepository.findById(dto.getCartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Verificar se o cartão está ativo
        if (!cartao.isAtivo()) throw new RuntimeException("Cartão desativado");

        // Verificar se a senha fornecida é válida
        if (!cartao.getSenha().equals(dto.getSenha())) throw new RuntimeException("Senha inválida");

        // Verificar se mudou o dia e zera o limite de gasto diário se necessário
        LocalDate hoje = LocalDate.now();
        if (!cartao.getDataUltimoUso().equals(hoje.toString()))
        {
            // Se for um novo dia, zera o gasto diário
            cartao.setGastoHoje(BigDecimal.ZERO);
            cartao.setDataUltimoUso(hoje.toString()); // Atualiza a data do último uso
        }

        // Verificar se o novo gasto não ultrapassa o limite diário
        BigDecimal novoGasto = cartao.getGastoHoje().add(dto.getValor());
        if (novoGasto.compareTo(cartao.getLimiteDiario()) > 0)
        {
            throw new RuntimeException("Limite diário excedido");
        }

        // Realizar o pagamento, descontando o valor da conta associada ao cartão
        Conta conta = cartao.getConta();
        conta.sacar(dto.getValor()); // Executa o saque na conta associada ao cartão

        // Atualizar o gasto diário do cartão
        cartao.setGastoHoje(novoGasto);

        // Salvar as alterações no banco de dados (conta e cartão)
        contaRepository.save(conta);
        debitoRepository.save(cartao);
    }

     //Método para alterar o limite diário do Cartão de Débito.
    public void alterarLimite(Long id, BigDecimal novoLimite)
    {
        // Buscar o Cartão de Débito pelo ID
        CartaoDebito cartao = debitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Alterar o limite diário do cartão
        cartao.setLimiteDiario(novoLimite);

        // Salvar as alterações no banco de dados
        debitoRepository.save(cartao);
    }

      //Método para alterar o status (ativo/inativo) do Cartão de Débito.
    public void trocarStatus(Long id, boolean ativo) {
        // Buscar o Cartão de Débito pelo ID
        CartaoDebito cartao = debitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Alterar o status do cartão
        cartao.setAtivo(ativo);

        // Salvar as alterações no banco de dados
        debitoRepository.save(cartao);
    }

     // Método para trocar a senha do Cartão de Débito.
    public void trocarSenha(Long id, String novaSenha) {
        // Buscar o Cartão de Débito pelo ID
        CartaoDebito cartao = debitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        // Alterar a senha do cartão
        cartao.setSenha(novaSenha);

        // Salvar as alterações no banco de dados
        debitoRepository.save(cartao);
    }
}