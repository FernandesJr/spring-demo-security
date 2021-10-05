package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("select p from Paciente p where p.usuario.email like :email")
    Optional<Paciente> findByEmailUsuario(String email);
}
