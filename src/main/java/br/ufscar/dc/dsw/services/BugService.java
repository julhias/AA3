package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.repositories.BugRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BugService {

    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    @Transactional(readOnly = true)
    public Bug buscarPorId(Integer id) {
        return bugRepository.findById(id).orElse(null);
    }

    @Transactional
    public void excluir(Integer id) {
        if (!bugRepository.existsById(id)) {
            throw new IllegalArgumentException("ID de bug inválido: " + id);
        }
        bugRepository.deleteById(id);
    }
}