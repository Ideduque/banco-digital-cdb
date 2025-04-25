package br.com.cdb.bancodigital.repository;

import br.com.cdb.bancodigital.entity.Conta;
import br.com.cdb.bancodigital.entity.Extrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtratoRepository extends JpaRepository<Extrato, Long> {
    List<Extrato> findByContaBancaria(Conta contaBancaria);
}
