package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.model.Projeto;
import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.services.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/projetos")
public class ProjetoAdminController {

    private final ProjetoService projetoService;

    public ProjetoAdminController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    private void addCommonAttributes(Model model, Projeto projeto) {
        model.addAttribute("projeto", projeto);
        List<Usuario> testadores = projetoService.buscarTodosTestadores();
        model.addAttribute("testadores", testadores);
        List<Long> assignedTesterIds = projeto.getUsuarios().stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());
        model.addAttribute("assignedTesterIds", assignedTesterIds);
    }

    @GetMapping("/listar")
    public String listar(@RequestParam(value = "sortBy", required = false, defaultValue = "nome") String sortBy, Model model) {
        model.addAttribute("projetos", projetoService.buscarTodos(sortBy));
        model.addAttribute("currentSortBy", sortBy);
        return "admin/projeto/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        addCommonAttributes(model, new Projeto());
        return "admin/projeto/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("projeto") Projeto projeto, BindingResult result,
                         @RequestParam(value = "selectedTesters", required = false) List<Long> selectedTesterIds,
                         RedirectAttributes attr) {

        if (result.hasErrors()) {
            addCommonAttributes(attr, projeto);
            return "admin/projeto/cadastro";
        }

        try {
            if (selectedTesterIds != null && !selectedTesterIds.isEmpty()) {
                List<Usuario> selectedUsers = selectedTesterIds.stream()
                        .map(id -> projetoService.buscarUsuarioPorId(id)) // CHANGED: Use the new public method
                        .filter(java.util.Objects::nonNull)
                        .collect(Collectors.toList());
                projeto.setUsuarios(selectedUsers);
            } else {
                projeto.setUsuarios(new java.util.ArrayList<>());
            }

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
        addCommonAttributes(model, projeto);
        return "admin/projeto/cadastro";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {
        try {
            projetoService.excluir(id);
            attr.addFlashAttribute("sucesso", "projeto.delete.success");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage());
        }
        return "redirect:/admin/projetos/listar";
    }
}