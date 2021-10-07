package com.fernandes.curso.security.repository.projection;

import com.fernandes.curso.security.domain.Especialidade;
import com.fernandes.curso.security.domain.Medico;
import com.fernandes.curso.security.domain.Paciente;

public interface HistoricoPaciente {

    Long getId();

    Paciente getPaciente();

    String getDataConsulta();

    Medico getMedico();

    Especialidade getEspecialidade();
}
