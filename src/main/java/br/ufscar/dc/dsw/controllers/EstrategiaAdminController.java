package br.ufscar.dc.dsw.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.dto.EstrategiaDTO;
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
        // 1. O service retorna uma lista de Entidades
        List<Estrategia> estrategias = service.buscarTodos();
        // 2. O controller mapeia a lista de Entidades para uma lista de DTOs
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
        // 1. O controller mapeia o DTO recebido para uma Entidade
        Estrategia estrategia = mapper.toEntity(dto);
        // 2. O service recebe e salva a Entidade
        Estrategia estrategiaSalva = service.salvar(estrategia);

        URI location = URI.create(String.format("/api/admin/estrategias/%d", estrategiaSalva.getId()));
        return ResponseEntity.created(location).body(mapper.toDTO(estrategiaSalva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstrategiaDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody EstrategiaDTO dto) {
        // 1. O controller mapeia o DTO recebido para uma Entidade com os novos dados
        Estrategia dadosParaAtualizar = mapper.toEntity(dto);
        // 2. O service recebe o ID e a Entidade com os dados para atualizar
        Estrategia estrategiaAtualizada = service.atualizar(id, dadosParaAtualizar);

        return ResponseEntity.ok(mapper.toDTO(estrategiaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

