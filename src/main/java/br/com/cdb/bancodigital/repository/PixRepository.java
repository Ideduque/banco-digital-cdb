package br.com.cdb.bancodigital.repository;

import br.com.cdb.bancodigital.entity.Pix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PixRepository extends JpaRepository<Pix, Long> {

    List<Pix> findByContaOrigemIdOrContaDestinoId(String contaOrigem_id, String contaDestino_id);
}
