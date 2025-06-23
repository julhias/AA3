package br.ufscar.dc.dsw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufscar.dc.dsw.model.Estrategia;

@Repository
public interface EstrategiaRepository extends JpaRepository<Estrategia, Integer> {
}