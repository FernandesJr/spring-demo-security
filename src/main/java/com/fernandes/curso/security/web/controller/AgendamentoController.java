package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Agendamento;
import com.fernandes.curso.security.domain.Especialidade;
import com.fernandes.curso.security.domain.Paciente;
import com.fernandes.curso.security.domain.PerfilTipo;
import com.fernandes.curso.security.service.AgendamentoService;
import com.fernandes.curso.security.service.EspecialidadeService;
import com.fernandes.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/agendar")
    public String agendar(Agendamento agendamento){
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{idMed}/data/{data}")
    public ResponseEntity<?> getHorariosMedico(@PathVariable("idMed") Long idMed,
                                               @PathVariable("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       LocalDate data){
        return ResponseEntity.ok(service.buscarHorariosPorMedicoEData(idMed, data));
    }

    @PostMapping("/salvar")
    public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user){
        Paciente paciente = pacienteService.buscarPorEmail(user.getUsername());
        String titulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService.buscarPorTitulos(
                new String[] {titulo})
                .stream().findFirst().get();
        agendamento.setPaciente(paciente);
        agendamento.setEspecialidade(especialidade);
        service.salvar(agendamento);
        attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
        return "redirect:/agendamentos/agendar";
    }

    //Mesma página para médicos e pacientes
    @GetMapping({"/historico/consultas","/historico/paciente"})
    public String historicoConsultas(){
        return "agendamento/historico-paciente";
    }

    @GetMapping("/datatables/server/historico")
    public ResponseEntity<?> retormoHistoricoConsultas(HttpServletRequest request, @AuthenticationPrincipal User user){
        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))){
            return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
        }
        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))){
            return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/editar/consulta/{id}")
    public String preEditar(@PathVariable Long id, ModelMap model, @AuthenticationPrincipal User user){
        Agendamento agendamento = service.buscarPorIdEUsuario(id, user.getUsername());
        model.addAttribute("agendamento", agendamento);
        return "agendamento/cadastro";
    }

    @PostMapping("/editar")
    public String editar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user){
        String titulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService.buscarPorTitulos(
                        new String[] {titulo})
                .stream().findFirst().get();
        agendamento.setEspecialidade(especialidade);
        service.editar(agendamento, user);
        attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso.");
        return "redirect:/agendamentos/agendar";
    }

    @GetMapping("/excluir/consulta/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attr){
        service.excluir(id);
        attr.addFlashAttribute("sucesso", "Consulta excluída com sucesso.");
        return "redirect:/agendamentos/historico/paciente";
    }
}
