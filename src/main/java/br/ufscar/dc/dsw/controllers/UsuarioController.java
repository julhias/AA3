package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.model.Usuario;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuario/lista";
    }

    @GetMapping("/cadastrar")
    public String cadastrar(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "usuario/cadastro";
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        attr.addFlashAttribute("sucesso", "Usuário salvo com sucesso.");
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuário inválido:" + id));
        model.addAttribute("usuario", usuario);
        return "usuario/cadastro";
    }

    @PostMapping("/editar")
    public String editar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            String senhaOriginal = usuarioRepository.findById(usuario.getId()).get().getSenha();
            usuario.setSenha(senhaOriginal);
        } else {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if (result.hasErrors()) {
            return "usuario/cadastro";
        }

        usuarioRepository.save(usuario);
        attr.addFlashAttribute("sucesso", "Usuário editado com sucesso.");
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
        if (!usuarioRepository.existsById(id)) {
            attr.addFlashAttribute("falha", "Falha ao excluir. ID do usuário não encontrado.");
        } else {
            usuarioRepository.deleteById(id);
            attr.addFlashAttribute("sucesso", "Usuário excluído com sucesso.");
        }
        return "redirect:/usuarios/listar";
    }
}