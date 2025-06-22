package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.services.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/projetos")
public class ProjetoAdminController {

    private final ProjetoService projetoService;

    public ProjetoAdminController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping("/listar")
    public String listar(@RequestParam(value = "sortBy", required = false, defaultValue = "nome") String sortBy, Model model) {
        model.addAttribute("projetos", projetoService.buscarTodos(sortBy));
        model.addAttribute("currentSortBy", sortBy); // To highlight current sort option
        return "admin/projeto/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("projeto", new Projeto());
        return "admin/projeto/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("projeto") Projeto projeto, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "admin/projeto/cadastro";
        }
        try {
            projetoService.salvar(projeto);
            attr.addFlashAttribute("sucesso", "projeto.save.success");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", "projeto.save.fail");
        }
        return "redirect:/admin/projetos/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Integer id, Model model, RedirectAttributes attr) {
        Projeto projeto = projetoService.buscarPorId(id);
        if (projeto == null) {
            attr.addFlashAttribute("falha", "projeto.not.found");
            return "redirect:/admin/projetos/listar";
        }
        model.addAttribute("projeto", projeto);
        return "admin/projeto/cadastro";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {
        try {
            projetoService.excluir(id);
            attr.addFlashAttribute("sucesso", "projeto.delete.success");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage()); // Exibe a mensagem de erro específica
        }
        return "redirect:/admin/projetos/listar";
    }
}