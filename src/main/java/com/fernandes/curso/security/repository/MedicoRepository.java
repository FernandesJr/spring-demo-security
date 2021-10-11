package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Query("select m from Medico m where m.usuario.id = :id")
    Optional<Medico> findByUsuarioId(Long id);

    @Query("select m from Medico m where m.usuario.email like :email")
    Optional<Medico> findByEmail(String email);

    @Query("select distinct m from Medico m " +
            "join m.especialidades e " +
            "where e.titulo like :especialidade " +
            "and m.usuario.ativo = true")
    List<Medico> findMedicosByEspecialidade(String especialidade);

    @Query("select m.id from Medico m " +
            "join m.especialidades e " +
            "join m.agendamentos a " +
            "where a.medico.id = :idMed AND a.especialidade.id = :idEsp")
    Optional<Long> hasEspecialidadeAgendada(Long idMed, Long idEsp);
}
