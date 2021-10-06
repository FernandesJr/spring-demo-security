package com.fernandes.curso.security.config;

import com.fernandes.curso.security.domain.PerfilTipo;
import com.fernandes.curso.security.service.UsuarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static String ADMIN   = PerfilTipo.ADMIN.getDesc();
    private static String MEDICO   = PerfilTipo.MEDICO.getDesc();
    private static String PACIENTE = PerfilTipo.PACIENTE.getDesc();

    @Autowired
    private UsuarioServico servico;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**","/css/**","/js/**", "/image/**").permitAll()
                .antMatchers("/","/home").permitAll()

                //Autorização para admim
                .antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
                .antMatchers("/u/**").hasAuthority(ADMIN)

                //Autorização para médicos e adm
                .antMatchers("/medicos/especialidade/titulo/*").hasAuthority(PACIENTE)
                .antMatchers("/medicos/salvar", "/medicos/editar", "/medicos/dados").hasAnyAuthority(MEDICO, ADMIN)
                .antMatchers("/medicos/**").hasAuthority(MEDICO)

                //Acesso privados especialidades
                .antMatchers("/especialidades/datatables/server/medico/**").hasAnyAuthority(MEDICO, ADMIN)
                .antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
                .antMatchers("/especialidades/**").hasAuthority(ADMIN)

                //Autorização para pacientes
                .antMatchers("/pacientes/**").hasAuthority(PACIENTE)
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login-error")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/acesso-negado"); //Tratando a exceção de restrição de acesso, está no HomeController
    }

    //Dizendo ao Spring que tipo de criptografia será utilizada E por qual classe ele irá validar login
    //Exemplo
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    //		System.out.println(new BCryptPasswordEncoder().encode("Fernandes"));
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
    }
}
