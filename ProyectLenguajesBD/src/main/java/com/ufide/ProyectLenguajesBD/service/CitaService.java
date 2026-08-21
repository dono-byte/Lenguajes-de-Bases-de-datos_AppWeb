package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> obtenerTodos() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Integer id) {
        return citaRepository.findById(id);
    }

    public List<Cita> obtenerPorPaciente(Integer pacienteId) {
        return citaRepository.findByPacientePkPaciente(pacienteId);
    }

    public List<Cita> obtenerPorRangoFecha(LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByFechaHoraBetween(inicio, fin);
    }

    public Cita guardar(Cita cita) {
        // Aquí podrías agregar validaciones de negocio
        return citaRepository.save(cita);
    }

    public Cita actualizar(Integer id, Cita citaActualizada) {
        return citaRepository.findById(id)
                .map(cita -> {
                    cita.setPaciente(citaActualizada.getPaciente());
                    cita.setConsultorio(citaActualizada.getConsultorio());
                    cita.setFechaHora(citaActualizada.getFechaHora());
                    cita.setDuracion(citaActualizada.getDuracion());
                    cita.setEstado(citaActualizada.getEstado());
                    return citaRepository.save(cita);
                })
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        citaRepository.deleteById(id);
    }
}