package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(login);

        if (usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o login: " + login);
        }

        Usuario usuario = usuarioOptional.get();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name());

        return new User(
                usuario.getLogin(),
                usuario.getSenha(),
                Collections.singletonList(authority)
        );
    }
}