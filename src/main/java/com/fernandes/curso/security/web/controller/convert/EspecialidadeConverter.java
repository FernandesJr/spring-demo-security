package com.fernandes.curso.security.web.controller.convert;

import com.fernandes.curso.security.domain.Especialidade;
import com.fernandes.curso.security.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class EspecialidadeConverter implements Converter<String[], Set<Especialidade>> {

    @Autowired
    private EspecialidadeService service;

    @Override
    public Set<Especialidade> convert(String[] titulos) {
        if(titulos.length > 0){
            Set<Especialidade> list = new HashSet<>();
            list.addAll(service.buscarPorTitulos(titulos));
            return list;
        }
        return null;
    }
}
