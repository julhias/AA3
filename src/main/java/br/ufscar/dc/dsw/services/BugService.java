package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.repositories.BugRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BugService {

    @Autowired
    private BugRepository bugRepository;

    @Transactional(readOnly = true)
    public Bug buscarPorId(Integer id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bug não encontrado com o ID: " + id));
    }

    public void excluir(Integer id) {
        if (!bugRepository.existsById(id)) {
            throw new EntityNotFoundException("Bug não encontrado com o ID: " + id);
        }
        bugRepository.deleteById(id);
    }
}