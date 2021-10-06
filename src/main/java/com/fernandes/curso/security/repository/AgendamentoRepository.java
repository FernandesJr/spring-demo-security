package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Agendamento;
import com.fernandes.curso.security.domain.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("select h " +
            "from Horario h " +
            "where not exists( " +
                "select a.horario.id " +
                    "from Agendamento a " +
                    "where " +
                        "a.medico.id = :idMed and " +
                        "a.dataConsulta = :data and " +
                        "a.horario.id = h.id " +
            ")")
    List<Horario> findByMedicoIdAndDataNotHorario(Long idMed, LocalDate data);
}
