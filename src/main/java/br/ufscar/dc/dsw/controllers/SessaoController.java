package br.ufscar.dc.dsw.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.model.Bug;
import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.model.Sessao;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import br.ufscar.dc.dsw.services.EstrategiaService;
import br.ufscar.dc.dsw.services.ProjetoService;
import br.ufscar.dc.dsw.services.SessaoService;
import jakarta.validation.Valid;

@Controller
public class SessaoController {

    private final SessaoService sessaoService;
    private final ProjetoService projetoService;
    private final EstrategiaService estrategiaService;
    private final UsuarioRepository usuarioRepository;

    public SessaoController(SessaoService sessaoService, ProjetoService projetoService, EstrategiaService estrategiaService, UsuarioRepository usuarioRepository) {
        this.sessaoService = sessaoService;
        this.projetoService = projetoService;
        this.estrategiaService = estrategiaService;
        this.usuarioRepository = usuarioRepository;
    }

    // R9: Listagem de sessões de um projeto
    @GetMapping("/projetos/{projetoId}/sessoes")
    public String listarSessoes(@PathVariable("projetoId") Integer projetoId, Model model) {
        model.addAttribute("projeto", projetoService.buscarPorId(projetoId));
        model.addAttribute("sessoes", sessaoService.buscarPorProjeto(projetoId));
        return "sessao/lista";
    }

    // R7: Exibir formulário de cadastro de sessão
    @GetMapping("/projetos/{projetoId}/sessoes/cadastrar")
    public String exibirFormularioCadastro(@PathVariable("projetoId") Integer projetoId, Model model) {
        Projeto projeto = projetoService.buscarPorId(projetoId);
        Sessao sessao = new Sessao();
        sessao.setProjeto(projeto);
        model.addAttribute("sessao", sessao);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        return "sessao/cadastro";
    }

    // R7: Salvar nova sessão
    @PostMapping("/sessoes/salvar")
    public String salvarSessao(@Valid @ModelAttribute("sessao") Sessao sessao, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("estrategias", estrategiaService.buscarTodas());
            return "sessao/cadastro";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario testador = usuarioRepository.findByLogin(username).orElseThrow();
        sessao.setTestador(testador);
        sessaoService.salvar(sessao);
        attr.addFlashAttribute("sucesso", "Sessão criada com sucesso!");
        return "redirect:/projetos/" + sessao.getProjeto().getId() + "/sessoes";
    }

    // R8: Detalhes da sessão (ponto central do ciclo de vida)
    @GetMapping("/sessoes/{id}")
    public String detalharSessao(@PathVariable("id") Integer id, Model model) {
        Sessao sessao = sessaoService.buscarPorId(id);
        model.addAttribute("sessao", sessao);
        model.addAttribute("bugs", sessaoService.buscarBugsPorSessao(id));
        model.addAttribute("novoBug", new Bug()); 
        return "sessao/detalhes";
    }
    
    // R8: Ação para iniciar a sessão (versão correta com segurança)
    @PostMapping("/sessoes/{id}/iniciar")
    public String iniciarSessao(@PathVariable("id") Integer id, RedirectAttributes attr) {
        Sessao sessao = sessaoService.buscarPorId(id);
        if (!isOwner(sessao)) {
            attr.addFlashAttribute("falha", "Acesso negado: você não é o dono desta sessão.");
            return "redirect:/sessoes/" + id;
        }
        
        try {
            sessaoService.iniciarSessao(id);
            attr.addFlashAttribute("sucesso", "Sessão iniciada!");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage());
        }
        return "redirect:/sessoes/" + id;
    }

    // R8: Ação para finalizar a sessão (versão correta com segurança)
    @PostMapping("/sessoes/{id}/finalizar")
    public String finalizarSessao(@PathVariable("id") Integer id, RedirectAttributes attr) {
        Sessao sessao = sessaoService.buscarPorId(id);
        if (!isOwner(sessao)) {
            attr.addFlashAttribute("falha", "Acesso negado: você não é o dono desta sessão.");
            return "redirect:/sessoes/" + id;
        }
        
        try {
            sessaoService.finalizarSessao(id);
            attr.addFlashAttribute("sucesso", "Sessão finalizada com sucesso.");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage());
        }
        return "redirect:/sessoes/" + id;
    }

    // R8: Ação para adicionar um bug (versão correta com segurança)
    @PostMapping("/sessoes/{id}/bugs")
    public String adicionarBug(@PathVariable("id") Integer id, @Valid @ModelAttribute("novoBug") Bug bug, BindingResult result, RedirectAttributes attr) {
        Sessao sessao = sessaoService.buscarPorId(id);
        if (!isOwner(sessao)) {
            attr.addFlashAttribute("falha", "Acesso negado: você não pode adicionar bugs nesta sessão.");
            return "redirect:/sessoes/" + id;
        }

        if(result.hasErrors()){
            attr.addFlashAttribute("falha", "Erro de validação ao adicionar bug.");
            return "redirect:/sessoes/" + id;
        }
        try {
            sessaoService.adicionarBug(id, bug);
            attr.addFlashAttribute("sucesso", "Bug registrado com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage());
        }
        return "redirect:/sessoes/" + id;
    }

    // Endpoint para EXIBIR o formulário de edição para o Admin
    @GetMapping("/sessoes/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("sessao", sessaoService.buscarPorId(id));
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        return "sessao/edicao";
    }

    // Endpoint para PROCESSAR a edição enviada pelo Admin
    @PostMapping("/sessoes/editar")
    public String editarSessao(@Valid @ModelAttribute("sessao") Sessao sessao, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "sessao/edicao";
        }
        sessaoService.editar(sessao);
        attr.addFlashAttribute("sucesso", "Sessão editada com sucesso!");
        return "redirect:/projetos/" + sessao.getProjeto().getId() + "/sessoes";
    }

    // Endpoint para EXCLUIR uma sessão
    @GetMapping("/sessoes/excluir/{id}")
    public String excluirSessao(@PathVariable("id") Integer id, RedirectAttributes attr) {
        Sessao sessao = sessaoService.buscarPorId(id);
        Integer projetoId = sessao.getProjeto().getId();
        try {
            sessaoService.excluir(id);
            attr.addFlashAttribute("sucesso", "Sessão excluída com sucesso.");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage());
        }
        return "redirect:/projetos/" + projetoId + "/sessoes";
    }

    // Método privado para verificar se o usuário logado é o "dono" da sessão
    private boolean isOwner(Sessao sessao) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return sessao.getTestador().getLogin().equals(username);
    }
}