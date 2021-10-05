package com.fernandes.curso.security.service;

import com.fernandes.curso.security.domain.Paciente;
import com.fernandes.curso.security.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Transactional(readOnly = true)
    public Paciente buscarPorEmail(String email){
        return repository.findByEmailUsuario(email).orElse(new Paciente());
    }

    @Transactional(readOnly = false)
    public void salvar(Paciente paciente) {
        repository.save(paciente);
    }

    @Transactional(readOnly = false)
    public void editar(Paciente paciente) {
        Paciente pDB = repository.findById(paciente.getId()).get();
        pDB.setNome(paciente.getNome());
        pDB.setDtNascimento(paciente.getDtNascimento());
        //OBS: O paciente que vêm do form mesmo com ID é considerado um Objeto transitório não pode ser salvo no DB
        //Pelo o Hibernate. já o pDB é um objeto persistente sendo assim o que fizer nele reflete no DB
    }
}
