package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.ProjetoDTO;
import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.services.SessaoService;
import br.ufscar.dc.dsw.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tester")
public class TesterController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private EntityMapper mapper;

    @GetMapping("/sessoes")
    public ResponseEntity<List<SessaoDTO>> minhasSessoes(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario testador = usuarioService.buscarPorLogin(userDetails.getUsername());
        List<SessaoDTO> dtos = sessaoService.buscarPorTestador(testador)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/projetos")
    public ResponseEntity<List<ProjetoDTO>> meusProjetos(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario testador = usuarioService.buscarPorLogin(userDetails.getUsername());
        List<ProjetoDTO> dtos = testador.getProjetos()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}