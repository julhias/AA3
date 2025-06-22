package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import org.springframework.data.domain.Sort; // Import for sorting
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Transactional
    public Projeto salvar(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public List<Projeto> buscarTodos() {
        return projetoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Projeto buscarPorId(Integer id) {
        return projetoRepository.findById(id).orElse(null);
    }

    @Transactional
    public void excluir(Integer id) {
        Projeto projeto = buscarPorId(id);
        if (projeto == null) {
            throw new IllegalArgumentException("ID de projeto inválido: " + id);
        }
        projetoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Projeto> buscarTodos(String sortBy) {
        Sort sort;
        switch (sortBy) {
            case "nome":
                sort = Sort.by(Sort.Direction.ASC, "nome");
                break;
            case "criadoEm":
                sort = Sort.by(Sort.Direction.DESC, "criadoEm"); // Descending for most recent first
                break;
            default:
                sort = Sort.by(Sort.Direction.ASC, "nome"); // Default sort by name
        }
        return projetoRepository.findAll(sort);
    }
}