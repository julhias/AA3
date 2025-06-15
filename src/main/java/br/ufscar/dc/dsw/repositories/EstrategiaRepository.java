package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.domain.Estrategia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstrategiaRepository extends JpaRepository<Estrategia, Integer> {
}