package br.com.mottugrid_java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Este é o único endpoint para o login via formulário (Web)
    @GetMapping("/login")
    public String login() {
        // Retorna o nome do template "login.html"
        // O Spring Security intercepta o POST para este endpoint.
        return "login";
    }
}
