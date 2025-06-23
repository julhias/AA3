package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import br.ufscar.dc.dsw.services.EstrategiaService;
import br.ufscar.dc.dsw.services.ProjetoService;
import br.ufscar.dc.dsw.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testador")
public class TesterController {

    @Autowired
    private EstrategiaRepository estrategiaRepository;
    @Autowired
    private SessaoService sessaoService;
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private UsuarioRepository usuarioRepository;

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
    public String minhasSessoes(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario testador = usuarioRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("Testador não encontrado."));
        model.addAttribute("sessoes", sessaoService.buscarPorTestador(testador));
        return "sessao/lista";
    }

    @GetMapping("/criar-sessao")
    public String criarSessao(Model model) {
        model.addAttribute("projetos", projetoService.buscarTodos());
        return "tester/escolher-projeto";
    }

    @GetMapping("/meus-projetos")
    public String meusProjetos(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario testador = usuarioRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("Testador não encontrado."));
        model.addAttribute("projetos", testador.getProjetos());
        return "tester/meus_projetos";
    }
}