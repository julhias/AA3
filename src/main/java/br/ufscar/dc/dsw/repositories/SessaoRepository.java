package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.model.enums.SessionStatus; // Added import for SessionStatus
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Integer> {

    List<Sessao> findByTestadorOrderByCriadoEmDesc(Usuario testador);

    List<Sessao> findByStatusAndInicioEmBefore(SessionStatus status, LocalDateTime dateTime);

    boolean existsByEstrategia(Estrategia estrategia);

}