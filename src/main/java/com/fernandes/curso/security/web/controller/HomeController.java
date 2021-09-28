package com.fernandes.curso.security.web.controller;

import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("alerta","erro");
		model.addAttribute("titulo","Credenciais inválidas!");
		model.addAttribute("texto","Login ou Senha incorreto, tentar novamente");
		model.addAttribute("subtexto","Acesso permitido apenas para cadastros ativos.");
		return "login";
	}


	//Recebe as requisições de error vinda das configurações security
	@GetMapping("/acesso-negado")
	public String acessoNegado(ModelMap model, HttpServletResponse response) {
		model.addAttribute("status",response.getStatus());
		model.addAttribute("error","Área restrita.");
		model.addAttribute("message","Acesso negado, para esta área ou ação.");
		return "error";
	}
}
