package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dto.UsuarioDTO;
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
        // O service é responsável por codificar a senha
        Usuario usuarioSalvo = service.salvar(usuario, dto.getSenha());

        URI location = URI.create(String.format("/api/admin/usuarios/%d", usuarioSalvo.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(usuarioSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        Usuario usuarioExistente = service.buscarPorId(id);
        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setLogin(dto.getLogin());
        usuarioExistente.setTipo(dto.getTipo());

        // O service lida com a lógica de atualizar a senha apenas se ela for fornecida
        Usuario usuarioAtualizado = service.salvar(usuarioExistente, dto.getSenha());

        return ResponseEntity.ok(mapper.toDTO(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}