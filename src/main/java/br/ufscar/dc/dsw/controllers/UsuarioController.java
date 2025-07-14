package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.UsuarioDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private EntityMapper mapper;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<UsuarioDTO> dtos = service.buscarTodos()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = mapper.toEntity(dto);
        Usuario usuarioSalvo = service.salvar(usuario, dto.getSenha());

        URI location = URI.create(String.format("/api/admin/usuarios/%d", usuarioSalvo.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(usuarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        Usuario usuarioExistente = service.buscarPorId(id);

        if (usuarioExistente == null) {
            return ResponseEntity.notFound().build();
        }

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setLogin(dto.getLogin());
        usuarioExistente.setTipo(dto.getTipo());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            service.salvar(usuarioExistente, dto.getSenha());
        } else {
            service.salvar(usuarioExistente);
        }
        return ResponseEntity.ok(mapper.toDTO(usuarioExistente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}