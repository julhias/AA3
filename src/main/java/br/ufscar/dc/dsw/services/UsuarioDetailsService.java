package br.ufscar.dc.dsw.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDetailsService.class);

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Este log mostra o início do ciclo
        logger.error("!!!!!!!!!! FUI CHAMADO PARA BUSCAR O USUÁRIO: {} !!!!!!!!!!", login);

        Usuario usuario = usuarioRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o login: " + login));
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name());

        return new org.springframework.security.core.userdetails.User(
            usuario.getLogin(),
            usuario.getSenha(),
            Collections.singletonList(authority)
        );
    }
}
