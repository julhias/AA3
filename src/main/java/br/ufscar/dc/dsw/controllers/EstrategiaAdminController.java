package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.model.Estrategia;
import br.ufscar.dc.dsw.services.EstrategiaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/estrategias")
public class EstrategiaAdminController {

    private final EstrategiaService estrategiaService;

    public EstrategiaAdminController(EstrategiaService estrategiaService) {
        this.estrategiaService = estrategiaService;
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        return "admin/estrategia/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("estrategia", new Estrategia());
        return "admin/estrategia/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("estrategia") Estrategia estrategia, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "admin/estrategia/cadastro";
        }
        try {
            estrategiaService.salvar(estrategia);
            attr.addFlashAttribute("sucesso", "estrategia.save.success");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", "estrategia.save.fail");
        }
        return "redirect:/admin/estrategias/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Integer id, Model model, RedirectAttributes attr) {
        Estrategia estrategia = estrategiaService.buscarPorId(id);
        if (estrategia == null) {
            attr.addFlashAttribute("falha", "estrategia.not.found");
            return "redirect:/admin/estrategias/listar";
        }
        model.addAttribute("estrategia", estrategia);
        return "admin/estrategia/cadastro";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Integer id, RedirectAttributes attr) {
        try {
            estrategiaService.excluir(id);
            attr.addFlashAttribute("sucesso", "estrategia.delete.success");
        } catch (Exception e) {
            attr.addFlashAttribute("falha", e.getMessage()); // Exibe a mensagem de erro específica
        }
        return "redirect:/admin/estrategias/listar";
    }
}