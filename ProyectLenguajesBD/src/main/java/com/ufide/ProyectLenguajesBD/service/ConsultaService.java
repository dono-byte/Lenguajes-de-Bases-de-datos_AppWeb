package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> obtenerTodos() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> obtenerPorId(Integer id) {
        return consultaRepository.findById(id);
    }

    public List<Consulta> obtenerPorExpediente(Integer expedienteId) {
        return consultaRepository.findByExpedientePkExpediente(expedienteId);
    }

    public List<Consulta> obtenerPorMedico(Integer medicoId) {
        return consultaRepository.findByPersonalMedicoPkPersonalMedico(medicoId);
    }

    public Consulta guardar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Consulta actualizar(Integer id, Consulta consultaActualizada) {
        return consultaRepository.findById(id)
                .map(consulta -> {
                    consulta.setExpediente(consultaActualizada.getExpediente());
                    consulta.setPersonalMedico(consultaActualizada.getPersonalMedico());
                    consulta.setDiagnostico(consultaActualizada.getDiagnostico());
                    consulta.setCita(consultaActualizada.getCita());
                    consulta.setFechaConsulta(consultaActualizada.getFechaConsulta());
                    consulta.setMotivo(consultaActualizada.getMotivo());
                    consulta.setObservaciones(consultaActualizada.getObservaciones());
                    return consultaRepository.save(consulta);
                })
                .orElseThrow(() -> new RuntimeException("Consulta no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        consultaRepository.deleteById(id);
    }
}