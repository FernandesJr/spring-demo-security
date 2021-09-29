package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("u")
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServico servico;

    @GetMapping("/novo/cadastro/usuario")
    public String cadastroUser(Usuario usuario){
        return "usuario/cadastro";
    }

    @GetMapping("/lista")
    public String listarUsers(){
        return "usuario/lista";
    }

    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> dadosTable(HttpServletRequest request){
        return ResponseEntity.ok(servico.buscarUsuarios(request));
    }
}
