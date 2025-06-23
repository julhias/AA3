package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.model.enums.Role;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;


    public ProjetoService(ProjetoRepository projetoRepository, UsuarioRepository usuarioRepository) {
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Projeto salvar(Projeto projeto) {
        if (projeto.getUsuarios() != null) {
            List<Usuario> managedUsers = projeto.getUsuarios().stream()
                    .map(u -> usuarioRepository.findById(u.getId()).orElse(null))
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
            projeto.setUsuarios(managedUsers);
        }
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
                sort = Sort.by(Sort.Direction.DESC, "criadoEm");
                break;
            default:
                sort = Sort.by(Sort.Direction.ASC, "nome");
        }
        return projetoRepository.findAll(sort);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodosTestadores() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getTipo() == Role.TESTER)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}