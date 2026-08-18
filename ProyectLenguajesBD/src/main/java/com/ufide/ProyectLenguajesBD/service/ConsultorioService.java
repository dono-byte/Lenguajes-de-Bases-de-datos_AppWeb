package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Consultorio;
import com.ufide.ProyectLenguajesBD.repository.ConsultorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultorioService {
    
    @Autowired
    private ConsultorioRepository consultorioRepository;

    public List<Consultorio> obtenerTodos() {
        return consultorioRepository.findAll();
    }

    public Optional<Consultorio> obtenerPorId(Integer id) {
        return consultorioRepository.findById(id);
    }

    public Consultorio guardar(Consultorio consultorio) {
        return consultorioRepository.save(consultorio);
    }

    public void eliminar(Integer id) {
        consultorioRepository.deleteById(id);
    }

    public Consultorio actualizar(Integer id, Consultorio consultorioActualizado) {
        return consultorioRepository.findById(id)
                .map(consultorio -> {
                    consultorio.setNumeroConsultorio(consultorioActualizado.getNumeroConsultorio());
                    consultorio.setLocalidad(consultorioActualizado.getLocalidad());
                    consultorio.setProvincia(consultorioActualizado.getProvincia());
                    return consultorioRepository.save(consultorio);
                })
                .orElse(null);
    }
}
