package br.ufscar.dc.dsw.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.model.Sessao; // << IMPORTAR

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {
    boolean existsByEstrategia(Estrategia estrategia);

    List<Sessao> findByProjetoIdOrderByCriadoEmDesc(Integer projetoId);
}