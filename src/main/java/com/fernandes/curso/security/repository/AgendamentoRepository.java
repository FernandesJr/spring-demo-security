package com.fernandes.curso.security.repository;

import com.fernandes.curso.security.domain.Agendamento;
import com.fernandes.curso.security.domain.Horario;
import com.fernandes.curso.security.repository.projection.HistoricoPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Query("select " +
                "a.id as id, " +
                "a.paciente as paciente, " +
                "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta, " +
                "a.medico as medico, " +
                "a.especialidade as especialidade " +
            "from Agendamento a " +
            "where a.paciente.usuario.email like :email")
    Page<HistoricoPaciente> findHistoricoByPacienteEmail(String email, Pageable pageable);

    @Query("select " +
            "a.id as id, " +
            "a.paciente as paciente, " +
            "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) as dataConsulta, " +
            "a.medico as medico, " +
            "a.especialidade as especialidade " +
            "from Agendamento a " +
            "where a.medico.usuario.email like :email")
    Page<HistoricoPaciente> findHistoricoByMedicoEmail(String email, Pageable pageable);

}
