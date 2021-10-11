package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.email = :email")
    public Usuario findByEmail(@Param("email") String email);

    @Query("select distinct u from Usuario u " +
            "join u.perfis p " +
            "where u.email like :search% OR p.desc like :search%")
    public Page<Usuario> findByEmailOrPerfil(Pageable pageable, @Param("search") String search);

    @Query("select distinct u from Usuario u " +
            "join u.perfis p " +
            "where u.id = :usuario AND p.id IN :perfis")
    Optional<Usuario> findByIdPerfis(@Param("usuario") Long usuarioId, @Param("perfis") Long[] perfisId);

    @Query("select u from Usuario u where u.email like :email AND u.ativo = true")
    Optional<Usuario> findByEmailAndAtivo(String email);
}
