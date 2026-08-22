package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.repository.PersonalMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonalMedicoService {

    @Autowired
    private PersonalMedicoRepository personalMedicoRepository;

    public List<PersonalMedico> obtenerTodos() {
    return personalMedicoRepository.findAllWithEspecialidades();
}

    public Optional<PersonalMedico> obtenerPorId(Integer id) {
        return personalMedicoRepository.findById(id);
    }

    public Optional<PersonalMedico> obtenerPorCodigoMedico(String codigoMedico) {
        return personalMedicoRepository.findByCodigoMedico(codigoMedico);
    }

    public Optional<PersonalMedico> obtenerPorCorreo(String correo) {
        return personalMedicoRepository.findByCorreoElectronico(correo);
    }

    public PersonalMedico guardar(PersonalMedico personalMedico) {
        return personalMedicoRepository.save(personalMedico);
    }

    public PersonalMedico actualizar(Integer id, PersonalMedico medicoActualizado) {
        return personalMedicoRepository.findById(id)
                .map(medico -> {
                    medico.setUsuario(medicoActualizado.getUsuario());
                    medico.setNombre(medicoActualizado.getNombre());
                    medico.setApellido(medicoActualizado.getApellido());
                    medico.setSegApellido(medicoActualizado.getSegApellido());
                    medico.setCodigoMedico(medicoActualizado.getCodigoMedico());
                    medico.setCorreoElectronico(medicoActualizado.getCorreoElectronico());
                    medico.setTelefono(medicoActualizado.getTelefono());
                    medico.setEstado(medicoActualizado.getEstado());
                    return personalMedicoRepository.save(medico);
                })
                .orElseThrow(() -> new RuntimeException("Personal médico no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        personalMedicoRepository.deleteById(id);
    }
    
}