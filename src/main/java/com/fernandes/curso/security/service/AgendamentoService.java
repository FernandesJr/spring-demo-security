package com.fernandes.curso.security.service;

import com.fernandes.curso.security.datatables.Datatables;
import com.fernandes.curso.security.datatables.DatatablesColunas;
import com.fernandes.curso.security.domain.Agendamento;
import com.fernandes.curso.security.domain.Horario;
import com.fernandes.curso.security.exception.AcessoNegadoException;
import com.fernandes.curso.security.repository.AgendamentoRepository;
import com.fernandes.curso.security.repository.projection.HistoricoPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private Datatables datatables;

    @Transactional(readOnly = true)
    public List<Horario> buscarHorariosPorMedicoEData(Long idMed, LocalDate data) {
        return repository.findByMedicoIdAndDataNotHorario(idMed, data);
    }

    @Transactional(readOnly = false)
    public void salvar(Agendamento agendamento) {
        repository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, HttpServletRequest request) {
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
        Pageable pageable = datatables.getPageable();
        Page<HistoricoPaciente> page = repository.findHistoricoByPacienteEmail(email, pageable);
        return datatables.getResponse(page);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarHistoricoPorMedicoEmail(String email, HttpServletRequest request) {
        datatables.setRequest(request);
        datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
        Pageable pageable = datatables.getPageable();
        Page<HistoricoPaciente> page = repository.findHistoricoByMedicoEmail(email, pageable);
        return datatables.getResponse(page);
    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return repository.findById(id).get();
    }

    @Transactional(readOnly = false) //A anotação transforma num objeto persistente
    public void editar(Agendamento agendamento, User user) {
        Agendamento ag = repository.findByIdAndPacienteOrMedico(agendamento.getId(), user.getUsername())
                .orElseThrow(() -> new AcessoNegadoException("Acesso negado ao usuário: " + user.getUsername())); //Objeto persistente o que alterar nele estará alterando no BD
        ag.setEspecialidade(agendamento.getEspecialidade());
        ag.setMedico(agendamento.getMedico());
        ag.setDataConsulta(agendamento.getDataConsulta());
        ag.setHorario(agendamento.getHorario());

    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorIdEUsuario(Long id, String email) {
        return repository.findByIdAndPacienteOrMedico(id, email)
                .orElseThrow(() -> new AcessoNegadoException("Acesso negado ao usuário: " + email));
    }

    @Transactional(readOnly = false)
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
