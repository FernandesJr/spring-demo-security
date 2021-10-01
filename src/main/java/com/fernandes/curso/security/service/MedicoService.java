package com.fernandes.curso.security.service;

import com.fernandes.curso.security.domain.Medico;
import com.fernandes.curso.security.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository repository;

    @Transactional(readOnly = true)
    public Medico buscarPorUsuarioId(Long id){
        //Caso não encontre no BD algum médico com determinado id devolve um zerado
        return repository.findByUsuarioId(id).orElse(new Medico());
    }

    @Transactional(readOnly = false)
    public void salvar(Medico medico) {
        repository.save(medico);
    }

    //Como o medico pode já ter relacionamento com especialidades, tem que ter um cuidado especial aqui
    //Caso o médico não edite as especialidades e submeta a edição, caso ela ja tenha no BD ao vir do
    //Form null as especialidade que subirem como null iram deletar os relacionamentos existente
    //Isso porque o Hibernate entende que você deseja excluir os relacionamentos já que você está passando null
    //Então antes de alterar verificamos se o form vem null ou não
    public void editar(Medico medico) {
        Medico m2 = repository.findById(medico.getId()).get(); //Objeto percistente
        m2.setNome(medico.getNome());
        m2.setCrm(medico.getCrm());
        m2.setDtInscricao(medico.getDtInscricao());
        if(!medico.getEspecialidades().isEmpty()){
            m2.setEspecialidades(medico.getEspecialidades());
        }
        repository.save(m2);
    }

    public Medico buscarPorEmail(String username) {
        return repository.findByEmail(username).orElse(new Medico());
    }
}
