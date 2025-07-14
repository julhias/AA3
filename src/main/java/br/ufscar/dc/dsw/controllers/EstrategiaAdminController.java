package br.ufscar.dc.dsw.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.dtos.EstrategiaDTO;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.services.EstrategiaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/estrategias")
public class EstrategiaAdminController {

    @Autowired
    private EstrategiaService service;

    @Autowired
    private EntityMapper mapper;

    @GetMapping
    public ResponseEntity<List<EstrategiaDTO>> listarTodos() {
        List<Estrategia> estrategias = service.buscarTodos();
        List<EstrategiaDTO> dtos = estrategias.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstrategiaDTO> buscarPorId(@PathVariable Integer id) {
        Estrategia estrategia = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(estrategia));
    }

    @PostMapping
    public ResponseEntity<EstrategiaDTO> criar(@Valid @RequestBody EstrategiaDTO dto) {
        Estrategia estrategia = mapper.toEntity(dto);
        Estrategia estrategiaSalva = service.salvar(estrategia);
        URI location = URI.create(String.format("/api/admin/estrategias/%d", estrategiaSalva.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(estrategiaSalva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstrategiaDTO> atualizarTotalmente(
            @PathVariable Integer id,
            @Valid @RequestBody EstrategiaDTO dto) {

        Estrategia dadosParaAtualizar = mapper.toEntity(dto);
        Estrategia estrategiaAtualizada = service.atualizarTotalmente(id, dadosParaAtualizar);

        return ResponseEntity.ok(mapper.toDTO(estrategiaAtualizada));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstrategiaDTO> atualizarParcialmente(
            @PathVariable Integer id,
            @RequestBody EstrategiaDTO dto) {
        Estrategia estrategiaAtualizada = service.atualizarParcialmente(id, dto);

        return ResponseEntity.ok(mapper.toDTO(estrategiaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}