package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.BugDTO;
import br.ufscar.dc.dsw.dtos.SessaoCreateDTO;
import br.ufscar.dc.dsw.dtos.SessaoDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.services.SessaoService;
import br.ufscar.dc.dsw.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EntityMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<SessaoDTO> buscarSessao(@PathVariable Integer id) {
        Sessao sessao = sessaoService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(sessao));
    }

    @PostMapping
    public ResponseEntity<SessaoDTO> criarSessao(@Valid @RequestBody SessaoCreateDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario testador = usuarioService.buscarPorLogin(userDetails.getUsername());
        Sessao novaSessao = sessaoService.criarSessao(dto, testador);

        URI location = URI.create(String.format("/api/sessoes/%d", novaSessao.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(novaSessao));
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarSessao(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        sessaoService.iniciarSessao(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarSessao(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        sessaoService.finalizarSessao(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessaoId}/bugs")
    public ResponseEntity<List<BugDTO>> listarBugsDaSessao(@PathVariable Integer sessaoId) {
        List<BugDTO> dtos = sessaoService.buscarBugsPorSessao(sessaoId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{sessaoId}/bugs")
    public ResponseEntity<BugDTO> adicionarBug(@PathVariable Integer sessaoId, @Valid @RequestBody BugDTO bugDto, @AuthenticationPrincipal UserDetails userDetails) {
        Bug novoBug = sessaoService.adicionarBug(sessaoId, bugDto, userDetails.getUsername());
        BugDTO novoBugDto = mapper.toDTO(novoBug);

        URI location = URI.create(String.format("/api/sessoes/%d/bugs/%d", sessaoId, novoBugDto.getId()));
        return ResponseEntity.created(location).body(novoBugDto);
    }
}
