package br.com.cdb.bancodigital.dto;

import br.com.cdb.bancodigital.entity.Cartao;
import br.com.cdb.bancodigital.enums.TipoCartao;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoDTO
{
    // ID único do cartão
    private String id;

    //Número do cartão (parcial, para não expor o número completo)
    private String numero;

    //Status do cartão (se está ativo ou desativado)
    private boolean ativo;

    // Tipo de cartão: crédito ou débito (definido pelo enum TipoCartao)
    private TipoCartao tipoCartao;

    // Limite de crédito do cartão (somente para cartão de crédito)
    private BigDecimal limiteCredito;

    //Limite diário do cartão (somente para cartão de débito)
    private BigDecimal limiteDiario;


    public static CartaoDTO fromEntity(Cartao cartao)
    {
        return new CartaoDTO(
                cartao.getId(),
                cartao.getNumero(),
                cartao.isAtivo(),
                cartao.getTipoCartao(),
                cartao.getLimiteCredito(),
                cartao.getLimiteDiario()
        );
    }

    /**
     * Valida se os valores monetários (limites de crédito ou débito) são válidos.
     * @return verdadeiro se os limites são válidos (não negativos), caso contrário, falso.
     */
    public boolean isValid()
    {
        return (limiteCredito == null || limiteCredito.compareTo(BigDecimal.ZERO) >= 0) &&
                (limiteDiario == null || limiteDiario.compareTo(BigDecimal.ZERO) >= 0);
    }

}