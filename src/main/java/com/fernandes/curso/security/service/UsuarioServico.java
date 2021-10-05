package com.fernandes.curso.security.service;

import com.fernandes.curso.security.datatables.Datatables;
import com.fernandes.curso.security.datatables.DatatablesColunas;
import com.fernandes.curso.security.domain.Perfil;
import com.fernandes.curso.security.domain.Usuario;
import com.fernandes.curso.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioServico implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private Datatables datatables;

    @Transactional(readOnly = true) //Somente leitura
    public Usuario buscarPorEmail(String email){
        return repository.findByEmail(email);
    }

    //Lembrando que o Security faz criptografia das senhas
    //Sendo assim a Class ConfigSecurity recebe qual o padrão de criptografia, para comparar neste momento
    //Na autenticação o Spring vem nesse método para autenticar
    @Override @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario user = buscarPorEmail(userName);
        return new User(
                user.getEmail(),
                user.getSenha(),
                AuthorityUtils.createAuthorityList(this.getAuthorities(user.getPerfis())) //Por causa desse get precisa adicionar o Transactional é nesse momento que o JPA vai buscar a lista no BD
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

    @Transactional(readOnly = true)
    public Map<String, Object> buscarUsuarios(HttpServletRequest request) {
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.USUARIOS);
        Page<Usuario> page = datatables.getSearch().isEmpty()
                ? repository.findAll(datatables.getPageable())
                : repository.findByEmailOrPerfil(datatables.getPageable(), datatables.getSearch());
        return datatables.getResponse(page);
    }

    public void salvarUsuario(Usuario usuario) {
        String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(crypt);
        repository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id).get();
    }

    public Usuario buscarPorIdPerfil(Long usuarioId, Long[] perfisId) {
        return repository.findByIdPerfis(usuarioId, perfisId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente."));
        //Caso a consulta volte vazia dispara um exceção
        //Enquanto isso a Class ExceptionController está ouvindo se a app dispara esse tipo para assim tratar.
    }

    public boolean validarSenha(String senhaDB, String senhaAtualForm) {
        return new BCryptPasswordEncoder().matches(senhaAtualForm, senhaDB);
    }

    public void redefinirSenha(Usuario u, String novaSenha) {
        u.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
        repository.save(u);
    }
}
