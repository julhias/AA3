package br.ufscar.dc.dsw.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufscar.dc.dsw.model.Bug;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
     List<Bug> findBySessaoIdOrderByTimestampDesc(Integer sessaoId);
}