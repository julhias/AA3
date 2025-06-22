package br.ufscar.dc.dsw.controllers; // Ou o pacote apropriado

import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testador")
public class TesterController {

    @Autowired
    private EstrategiaRepository estrategiaRepository;

    @GetMapping("/home")
    public String homeTestador() {
        return "tester/home";
    }

    @GetMapping("/estrategias")
    public String listarEstrategiasTestador(Model model) {
        model.addAttribute("estrategias", estrategiaRepository.findAll());
        return "public/lista_estrategias";
    }

    @GetMapping("/minhas-sessoes")
    public String minhasSessoes() {
        return "testador/minhas-sessoes";
    }

    @GetMapping("/criar-sessao")
    public String criarSessao() {
        return "testador/criar-sessao";
    }
}