package br.ufscar.dc.dsw.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.config.JwtUtil;
import br.ufscar.dc.dsw.dtos.AuthenticationRequest;
import br.ufscar.dc.dsw.dtos.AuthenticationResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        logger.info(">>> AuthController: Recebida requisição de login para o usuário: {}", authenticationRequest.getLogin());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getSenha())
        );

        logger.info(">>> AuthController: Usuário autenticado com sucesso pelo AuthenticationManager.");

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        final String jwt = jwtUtil.generateToken(userDetails);
        logger.info(">>> AuthController: Token JWT gerado para o usuário.");

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}