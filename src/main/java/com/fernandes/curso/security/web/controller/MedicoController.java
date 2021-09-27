package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Medico;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @GetMapping({"/dados"})
    public String dados(Medico medico, ModelMap model){
        return "medico/cadastro";
    }
}
