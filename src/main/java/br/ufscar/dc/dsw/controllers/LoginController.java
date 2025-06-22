package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private EstrategiaRepository estrategiaRepository;

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/estrategias-publico")
    public String listarEstrategiasPublicas(Model model) {
        model.addAttribute("estrategias", estrategiaRepository.findAll());
        return "public/lista_estrategias";
    }
}