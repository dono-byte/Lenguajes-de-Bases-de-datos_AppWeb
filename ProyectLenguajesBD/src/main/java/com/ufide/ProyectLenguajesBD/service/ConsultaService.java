package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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

    public void eliminar(Integer id) {
        consultaRepository.deleteById(id);
    }

    public Consulta actualizar(Integer id, Consulta consultaActualizada) {
        return consultaRepository.findById(id)
                .map(consulta -> {
                    consulta.setMotivo(consultaActualizada.getMotivo());
                    consulta.setObservaciones(consultaActualizada.getObservaciones());
                    return consultaRepository.save(consulta);
                })
                .orElse(null);
    }
}
