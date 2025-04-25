package br.com.cdb.bancodigital.entity;

import br.com.cdb.bancodigital.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    protected Cliente cliente;

    private String numero;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    protected BigDecimal saldo = BigDecimal.ZERO;

    public void depositar(BigDecimal valor) {
        saldo = saldo.add(valor);
    }

    public boolean sacar(BigDecimal valor) {
        if (saldo.compareTo(valor) >= 0) {
            saldo = saldo.subtract(valor);
            return true;
        }
        return false;
    }

    public abstract void processarMensalidade();
}