package com.fernandes.curso.security.web.controller.convert;

import com.fernandes.curso.security.domain.Perfil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {
    @Override
    public List<Perfil> convert(String[] strings) {
        List<Perfil> perfis = new ArrayList<>();

        //Do formulario sempre vem o valor zero porque caso o usuário selecione apenas um perfil o zero obriga a vir em formato de list
        //porém ele têm que ser descartado
        for(String id: strings){
            if(!id.equals("0")){
                perfis.add(new Perfil(Long.valueOf(id)));
            }
        }
        return perfis;
    }
}
