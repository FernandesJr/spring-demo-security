package com.fernandes.curso.security.service;

import com.fernandes.curso.security.domain.Perfil;
import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServico implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional(readOnly = true) //Somente leitura
    public Usuario buscarPorEmail(String email){
        return repository.findByEmail(email);
    }

    //Lembrando que o Security faz criptografia das senhas
    //Sendo assim a Class ConfigSecurity recebe qual o padrão de criptografia, para comparar neste momento
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario user = buscarPorEmail(userName);
        return new User(
                user.getEmail(),
                user.getSenha(),
                AuthorityUtils.createAuthorityList(this.getAuthorities(user.getPerfis()))
        );
    }

    //Convertendo a List de perfis para um array
    private String[] getAuthorities(List<Perfil> perfis){
        String[] authorities = new String[perfis.size()];
        for (int i = 0; i < perfis.size(); i++) {
            authorities[i] = perfis.get(i).getDesc();
        }
        return authorities;
    }
}
