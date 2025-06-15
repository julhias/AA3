package br.ufscar.dc.dsw.repositories;

import br.ufscar.dc.dsw.domain.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
}