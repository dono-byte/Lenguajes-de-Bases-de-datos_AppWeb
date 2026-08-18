package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    
    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    public Optional<Paciente> obtenerPorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula);
    }

    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }

    public Paciente actualizar(Integer id, Paciente pacienteActualizado) {
        return pacienteRepository.findById(id)
                .map(paciente -> {
                    paciente.setNombre(pacienteActualizado.getNombre());
                    paciente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
                    paciente.setGenero(pacienteActualizado.getGenero());
                    paciente.setTelefono(pacienteActualizado.getTelefono());
                    paciente.setDireccion(pacienteActualizado.getDireccion());
                    return pacienteRepository.save(paciente);
                })
                .orElse(null);
    }
}
