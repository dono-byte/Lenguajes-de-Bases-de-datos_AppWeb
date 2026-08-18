package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.repository.ExpedienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExpedienteService {
    
    @Autowired
    private ExpedienteRepository expedienteRepository;

    public List<Expediente> obtenerTodos() {
        return expedienteRepository.findAll();
    }

    public Optional<Expediente> obtenerPorId(Integer id) {
        return expedienteRepository.findById(id);
    }

    public Optional<Expediente> obtenerPorPaciente(Integer pacienteId) {
        return expedienteRepository.findByPacienteId(pacienteId);
    }

    public Expediente guardar(Expediente expediente) {
        return expedienteRepository.save(expediente);
    }

    public void eliminar(Integer id) {
        expedienteRepository.deleteById(id);
    }
}
