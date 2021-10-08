package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Medico;
import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.service.MedicoService;
import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @Autowired
    private UsuarioServico usuarioService;

    @GetMapping({"/dados"})
    public String dados(Medico medico, ModelMap model, @AuthenticationPrincipal User user){
        if(medico.hasNotId()){
            model.addAttribute("medico", service.buscarPorEmail(user.getUsername()));
        }
        return "medico/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Medico medico, RedirectAttributes attr, @AuthenticationPrincipal User user){
        if (medico.hasNotId()){
            Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
            medico.setUsuario(usuario);
        }
        service.salvar(medico);
        attr.addFlashAttribute("medico", medico);
        attr.addFlashAttribute("sucesso", "Operação feita com sucesso.");
        return "redirect:/medicos/dados";
    }

    @PostMapping("/editar")
    public String editar(Medico medico, RedirectAttributes attr){
        service.editar(medico);
        attr.addFlashAttribute("medico", medico);
        attr.addFlashAttribute("sucesso", "Operação feita com sucesso.");
        return "redirect:/medicos/dados";
    }

    @GetMapping("/id/{idMed}/excluir/especializacao/{idEsp}")
    public String excluirEspecialicaoDeMedico(@PathVariable("idMed") Long idMed,
                                              @PathVariable("idEsp") Long idEsp, RedirectAttributes attr){
        if(service.isConsutasAgendadas(idMed, idEsp)){
            attr.addFlashAttribute("falha", "Especialidade contêm consultas agendadas.");
        }else {
            service.excluirEspecialidadeDeMedico(idMed, idEsp);
            attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso.");
        }
        return "redirect:/medicos/dados";
    }

    @GetMapping("/especialidade/titulo/{especialidade}")
    public ResponseEntity<?> getMedicosPorEspecialidade(@PathVariable String especialidade){
        return ResponseEntity.ok(service.buscarMedicosPorEspecialidade(especialidade));
    }
}
