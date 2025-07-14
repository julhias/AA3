package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.ProjetoDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.services.ProjetoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/projetos")
public class ProjetoAdminController {

    @Autowired
    private ProjetoService service;

    @Autowired
    private EntityMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> listar(@RequestParam(value = "sortBy", required = false, defaultValue = "nome") String sortBy) {
        List<ProjetoDTO> dtos = service.buscarTodos(sortBy)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoDTO> buscarPorId(@PathVariable Integer id) {
        Projeto projeto = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(projeto));
    }

    @PostMapping
    public ResponseEntity<ProjetoDTO> criar(@Valid @RequestBody ProjetoDTO dto) {
        Projeto projeto = mapper.toEntity(dto);
        Projeto projetoSalvo = service.salvar(projeto, dto.getTestadoresIds());

        URI location = URI.create(String.format("/api/admin/projetos/%d", projetoSalvo.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(projetoSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody ProjetoDTO dto) {
        Projeto projetoExistente = service.buscarPorId(id);
        projetoExistente.setNome(dto.getNome());
        projetoExistente.setDescricao(dto.getDescricao());

        Projeto projetoAtualizado = service.salvar(projetoExistente, dto.getTestadoresIds());
        return ResponseEntity.ok(mapper.toDTO(projetoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}