package com.fernandes.curso.security.web.controller.exception;

import com.fernandes.curso.security.exception.AcessoNegadoException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class) //Ouvindo determinada exceção
    public ModelAndView usuarioInexistente(UsernameNotFoundException ex){
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 404);
        model.addObject("error", "Ação não pode ser executada.");
        model.addObject("message", ex.getMessage());
        return model;
    }

    @ExceptionHandler(AcessoNegadoException.class) //Ouvindo determinada exceção
    public ModelAndView acessoNegado(AcessoNegadoException ex){
        ModelAndView model = new ModelAndView("error");
        model.addObject("status", 403);
        model.addObject("error", "Ação não pode ser executada.");
        model.addObject("message", ex.getMessage());
        return model;
    }
}
