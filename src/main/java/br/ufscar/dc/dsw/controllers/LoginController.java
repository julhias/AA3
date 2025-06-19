package br.ufscar.dc.dsw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Este mapeamento não é mais o principal destino, mas pode ser mantido
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}