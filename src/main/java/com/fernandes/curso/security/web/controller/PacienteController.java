package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Paciente;
import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.service.PacienteService;
import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @Autowired
    private UsuarioServico usuarioServico;


    @GetMapping("/dados")
    public String dados(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){
        paciente = service.buscarPorEmail(user.getUsername());
        if(paciente.hasNotId()){
            paciente.setUsuario(new Usuario(user.getUsername()));
        }
        model.addAttribute("paciente", paciente);
        return "paciente/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){
        Usuario uDB = usuarioServico.buscarPorEmail(user.getUsername());
        if (usuarioServico.isValidarSenha(paciente.getUsuario().getSenha(), uDB.getSenha())){
            paciente.setUsuario(uDB);
            service.salvar(paciente);
            model.addAttribute("sucesso", "Seus dados foram salvos com sucesso.");
        }else{
            model.addAttribute("falha", "Senha não confere. Por favor verifique.");
        }
        return "paciente/cadastro";
    }

    @PostMapping("/editar")
    public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user){
        Usuario uDB = usuarioServico.buscarPorEmail(user.getUsername());
        if (usuarioServico.isValidarSenha(paciente.getUsuario().getSenha(), uDB.getSenha())){
            service.editar(paciente);
            model.addAttribute("sucesso", "Seus dados foram editados com sucesso.");
        }else{
            model.addAttribute("falha", "Senha não confere. Por favor verifique.");
        }
        return "paciente/cadastro";
    }
}
