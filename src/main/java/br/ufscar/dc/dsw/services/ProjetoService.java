package br.ufscar.dc.dsw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.Role;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Projeto salvar(Projeto projeto, List<Long> testadoresIds) {
        // Busca as entidades Usuario a partir dos IDs
        if (testadoresIds != null && !testadoresIds.isEmpty()) {
            List<Usuario> testadores = usuarioRepository.findAllById(testadoresIds);
            projeto.setUsuarios(testadores);
        } else {
            projeto.setUsuarios(new ArrayList<>());
        }
        return projetoRepository.save(projeto);
    }

    @Transactional(readOnly = true)
    public List<Projeto> buscarTodos(String sortBy) {
        Sort sort = switch (sortBy) {
            case "criadoEm" -> Sort.by(Sort.Direction.DESC, "criadoEm");
            default -> Sort.by(Sort.Direction.ASC, "nome");
        };
        return projetoRepository.findAll(sort);
    }
    
    @Transactional(readOnly = true)
    public List<Projeto> buscarTodos() {
        return this.buscarTodos("nome");
    }

    @Transactional(readOnly = true)
    public Projeto buscarPorId(Integer id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + id));
    }

    public void excluir(Integer id) {
        if (!projetoRepository.existsById(id)) {
            throw new EntityNotFoundException("Projeto não encontrado com o ID: " + id);
        }
        projetoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodosTestadores() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getTipo() == Role.TESTER)
                .collect(Collectors.toList());
    }
}
