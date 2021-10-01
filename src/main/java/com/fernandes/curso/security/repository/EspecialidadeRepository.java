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

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    @Query("select e from Especialidade e where e.titulo like :search%")
    Page<?> findAllByTitulo(Pageable pageable, @Param("search") String search);

    @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
    List<String> findByTermo(String termo);
}
