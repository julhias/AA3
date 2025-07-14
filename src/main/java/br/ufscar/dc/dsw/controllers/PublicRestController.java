package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.EstrategiaDTO;
import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.services.EstrategiaService;
import br.ufscar.dc.dsw.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicRestController {

    @Autowired
    private EstrategiaService estrategiaService;

    @Autowired
    private EntityMapper mapper;

    @GetMapping("/estrategias")
    public ResponseEntity<List<EstrategiaDTO>> listarTodasEstrategias() {
        List<Estrategia> estrategias = estrategiaService.buscarTodos();
        List<EstrategiaDTO> dtos = estrategias.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/estrategias/{id}")
    public ResponseEntity<EstrategiaDTO> buscarEstrategiaPorId(@PathVariable Integer id) {
        Estrategia estrategia = estrategiaService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDTO(estrategia));
    }
}