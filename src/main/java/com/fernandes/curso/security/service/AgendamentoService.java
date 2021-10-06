package com.fernandes.curso.security.service;

import com.fernandes.curso.security.domain.Agendamento;
import com.fernandes.curso.security.domain.Horario;
import com.fernandes.curso.security.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Transactional(readOnly = true)
    public List<Horario> buscarHorariosPorMedicoEData(Long idMed, LocalDate data) {
        return repository.findByMedicoIdAndDataNotHorario(idMed, data);
    }

    @Transactional(readOnly = false)
    public void salvar(Agendamento agendamento) {
        repository.save(agendamento);
    }
}
