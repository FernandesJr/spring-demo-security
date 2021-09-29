package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Especialidade;
import com.fernandes.curso.security.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        if(especialidade.getId() != null){
            attr.addFlashAttribute("sucesso", "Especialidade editada com sucesso!");
        }else{
            attr.addFlashAttribute("sucesso", "Especialidade salva com sucesso!");
        }
        service.salvar(especialidade);
        return "redirect:/especialidades";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> consultaTable(HttpServletRequest request){
        return ResponseEntity.ok(service.getDataTable(request));
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model){
        model.addAttribute("especialidade", service.buscarPorId(id));
        return "especialidade/especialidade";
    }
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attr){
        service.excluir(id);
        attr.addFlashAttribute("sucesso", "Especialidade excluída com sucesso.");
        return "redirect:/especialidades";
    }
}
