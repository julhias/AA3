package br.ufscar.dc.dsw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario, String senhaNaoCodificada) {
        // Se uma senha for fornecida, codifique-a antes de salvar
        if (senhaNaoCodificada != null && !senhaNaoCodificada.isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(senhaNaoCodificada));
        }
        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o login: " + login));
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        // Adicionar lógica de verificação se o usuário tem dependências
        repository.deleteById(id);
    }
}