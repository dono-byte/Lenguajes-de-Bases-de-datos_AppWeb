package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.repository.ExpedienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        return expedienteRepository.findByPacientePkPaciente(pacienteId);
    }

    public Expediente guardar(Expediente expediente) {
        return expedienteRepository.save(expediente);
    }

    public Expediente actualizar(Integer id, Expediente expedienteActualizado) {
        return expedienteRepository.findById(id)
                .map(expediente -> {
                    expediente.setPaciente(expedienteActualizado.getPaciente());
                    expediente.setFechaCreacion(expedienteActualizado.getFechaCreacion());
                    return expedienteRepository.save(expediente);
                })
                .orElseThrow(() -> new RuntimeException("Expediente no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        expedienteRepository.deleteById(id);
    }
}