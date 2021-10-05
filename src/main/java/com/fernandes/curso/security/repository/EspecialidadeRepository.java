package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<?> findAllByTitulo(Pageable pageable, @Param("search") String search);

    @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
    List<String> findByTermo(String termo);

    @Query("select e from Especialidade e where e.titulo IN :titulos")
    Set<Especialidade> findByTitulos(String[] titulos);

    @Query("select e from Especialidade e " +
            "join e.medicos m " +
            "where m.id = :idMedico")
    Page<Especialidade> findByMedico(Pageable pageable, Long idMedico);
}
