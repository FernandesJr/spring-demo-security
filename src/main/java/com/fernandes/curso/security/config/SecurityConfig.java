package com.fernandes.curso.security.config;

import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServico servico;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**","/css/**","/js/**", "/image/**").permitAll()
                .antMatchers("/","/home").permitAll()

                //Autorização para admim
                .antMatchers("/u/**").hasAuthority("ADMIN")

                //Autorização para médicos
                .antMatchers("/medicos/**").hasAuthority("MEDICO")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login-error")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/");
    }

    //Dizendo ao Spring que tipo de criptografia será utilizada
    //Exemplo
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
    }
}
