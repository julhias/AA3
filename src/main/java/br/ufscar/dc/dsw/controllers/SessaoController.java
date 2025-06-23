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
    @PostMapping("/sessoes/{sessaoId}/bugs")
public String adicionarBug(@PathVariable("sessaoId") Integer sessaoId,
                           @Valid @ModelAttribute("novoBug") Bug bug,
                           BindingResult result,
                           Model model,
                           RedirectAttributes attr) {

    // Busca a sessão usando a variável de caminho correta
    Sessao sessao = sessaoService.buscarPorId(sessaoId);

    // Verificação de segurança para garantir que o usuário é o dono da sessão
    if (!isOwner(sessao)) {
        attr.addFlashAttribute("falha", "Acesso negado: você não pode adicionar bugs nesta sessão.");
        // Usa a variável correta no redirecionamento
        return "redirect:/sessoes/" + sessaoId;
    }

    // Se o formulário tiver erros de validação
    if (result.hasErrors()) {
        // Re-popula o model com os dados necessários para a página de detalhes
        model.addAttribute("sessao", sessao);
        model.addAttribute("bugs", sessaoService.buscarBugsPorSessao(sessaoId));
        
        // Retorna para a página de detalhes para mostrar os erros (NÃO redireciona)
        return "sessao/detalhes";
    }

    try {
        // Passa o ID da sessão e o objeto bug para o serviço
        sessaoService.adicionarBug(sessaoId, bug);
        attr.addFlashAttribute("sucesso", "Bug registrado com sucesso!");
    } catch (Exception e) {
        attr.addFlashAttribute("falha", e.getMessage());
    }

    // Se tudo deu certo, redireciona para a página de detalhes da sessão
    return "redirect:/sessoes/" + sessaoId;
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

    private boolean isOwner(Sessao sessao) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return sessao.getTestador().getLogin().equals(username);
    }
}