package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("u")
@Controller
public class UsuarioController {

    @GetMapping("/novo/cadastro/usuario")
    public String cadastroUser(Usuario usuario){
        return "usuario/cadastro";
    }
}
