package com.fernandes.curso.security.web.controller;

import com.fernandes.curso.security.domain.Medico;
import com.fernandes.curso.security.domain.Perfil;
import com.fernandes.curso.security.domain.PerfilTipo;
import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.service.MedicoService;
import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RequestMapping("u")
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServico servico;

    @Autowired
    private MedicoService medicoServico;

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

    @PostMapping("/cadastro/salvar")
    public String salvarUsuario(Usuario usuario, RedirectAttributes atrr){
        List<Perfil> perfis = usuario.getPerfis();
        //Não pode ter mais de duas seleções, nem adm paciente, nem medico paciente
        if(perfis.size() > 2 ||
            perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
            perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))){

            atrr.addFlashAttribute("falha", "Paciente não pode ser Adim ou Médico!");
            atrr.addFlashAttribute("usuario", usuario);
        }else{
            try {
                if(usuario.getId() != null){
                    atrr.addFlashAttribute("sucesso", "Usuário editado com sucesso!");
                }else{
                    atrr.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
                }
                servico.salvarUsuario(usuario);
            }catch (DataAccessException e){
                atrr.addFlashAttribute("falha", "Usuário não cadastrado, email já existente.");
            }
        }
        return "redirect:/u/novo/cadastro/usuario";
    }

    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView PreEditarUsuario(@PathVariable("id") Long id){
        return new ModelAndView("usuario/cadastro", "usuario", servico.buscarPorId(id));
    }

    @GetMapping("editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView PreEditarDadosPessoais(@PathVariable("id") Long usuarioId,
                                               @PathVariable("perfis") Long[] perfisId){
        Usuario us = servico.buscarPorIdPerfil(usuarioId, perfisId);

        if(us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
            !us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
            ModelAndView model = new ModelAndView("usuario/cadastro");
            model.addObject("usuario", us);
            return model;

        }else if(us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))){
            Medico medico = medicoServico.buscarPorUsuarioId(usuarioId);
            return medico.hasNotId()
                    ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
                    : new ModelAndView("medico/cadastro", "medico",medico);


        }else if(us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))){
            ModelAndView model = new ModelAndView("error");
            model.addObject("status", 403);
            model.addObject("error", "Área restrita");
            model.addObject("message", "Os dados de Pacientes são restritos a ele.");
            return model;
        }
        return new ModelAndView("redirect:/u/lista");
    }

    @GetMapping("/editar/senha")
    public String preEditarSenha(){
        return "usuario/editar-senha";
    }

    @PostMapping("/confirmar/senha")
    public String alterarSenha(@RequestParam("senha1") String s1,
                               @RequestParam("senha2") String s2,
                               @RequestParam("senha3") String s3,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes attr){
        if(!s1.equals(s2)){
            attr.addFlashAttribute("falha", "Nova senha não confere, por favor verifique.");
            return "redirect:/u/editar/senha";
        }

        Usuario u = servico.buscarPorEmail(user.getUsername());
        if(!servico.isValidarSenha(s3, u.getSenha())){
            attr.addFlashAttribute("falha", "Senha do usuário não confere.");
            return "redirect:/u/editar/senha";
        }

        servico.redefinirSenha(u, s1);
        attr.addFlashAttribute("sucesso", "Senha alterada com sucesso.");
        return "redirect:/u/editar/senha";
    }
}
