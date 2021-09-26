package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.email = :email")
    public Usuario findByEmail(@Param("email") String email);
}
