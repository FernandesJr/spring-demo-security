package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Especialidade;
import com.fernandes.curso.security.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService service;

    @GetMapping({"","/"})
    public String cadastroEspecialidade(Especialidade especialidade){
        return "especialidade/especialidade";
    }

    @PostMapping("/salvar")
    public String salvar(Especialidade especialidade, RedirectAttributes attr){
        service.salvar(especialidade);
        attr.addFlashAttribute("sucesso", "Especialidade salva com sucesso!");
        return "redirect:/especialidades";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> consultaTable(HttpServletRequest request){
        return ResponseEntity.ok(service.getDataTable(request));
    }
}
