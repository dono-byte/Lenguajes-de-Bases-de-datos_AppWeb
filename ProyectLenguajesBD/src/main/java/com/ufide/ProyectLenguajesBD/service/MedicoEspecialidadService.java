package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.MedicoEspecialidad;
import com.ufide.ProyectLenguajesBD.repository.MedicoEspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicoEspecialidadService {

    @Autowired
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    public List<MedicoEspecialidad> obtenerTodos() {
        return medicoEspecialidadRepository.findAll();
    }

    public Optional<MedicoEspecialidad> obtenerPorId(Integer id) {
        return medicoEspecialidadRepository.findById(id);
    }

    public MedicoEspecialidad guardar(MedicoEspecialidad medicoEspecialidad) {
        return medicoEspecialidadRepository.save(medicoEspecialidad);
    }

    public MedicoEspecialidad actualizar(Integer id, MedicoEspecialidad medicoEspecialidadActualizada) {
        return medicoEspecialidadRepository.findById(id)
                .map(me -> {
                    me.setPersonalMedico(medicoEspecialidadActualizada.getPersonalMedico());
                    me.setEspecialidad(medicoEspecialidadActualizada.getEspecialidad());
                    return medicoEspecialidadRepository.save(me);
                })
                .orElseThrow(() -> new RuntimeException("Relación médico-especialidad no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        medicoEspecialidadRepository.deleteById(id);
    }
}