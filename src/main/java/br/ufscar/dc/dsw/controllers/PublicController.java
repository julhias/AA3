package br.ufscar.dc.dsw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufscar.dc.dsw.services.EstrategiaService;

@Controller
@RequestMapping("/public") // Agrupa todas as rotas deste controller sob o prefixo /public
public class PublicController {

    private final EstrategiaService estrategiaService;

    public PublicController(EstrategiaService estrategiaService) {
        this.estrategiaService = estrategiaService;
    }

    // Este método responde à URL: /public/estrategias
    @GetMapping("/estrategias")
    public String listarEstrategiasPublico(Model model) {
        // Busca todas as estratégias no banco
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        // Retorna o nome do arquivo HTML que deve ser renderizado
        return "public/lista_estrategias";
    }
}