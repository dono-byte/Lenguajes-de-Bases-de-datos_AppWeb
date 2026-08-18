package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Diagnostico;
import com.ufide.ProyectLenguajesBD.repository.DiagnosticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DiagnosticoService {
    
    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    public List<Diagnostico> obtenerTodos() {
        return diagnosticoRepository.findAll();
    }

    public Optional<Diagnostico> obtenerPorId(Integer id) {
        return diagnosticoRepository.findById(id);
    }

    public Optional<Diagnostico> obtenerPorCodigoCIE10(String codigoCie10) {
        return diagnosticoRepository.findByCodigoCie10(codigoCie10);
    }

    public Diagnostico guardar(Diagnostico diagnostico) {
        return diagnosticoRepository.save(diagnostico);
    }

    public void eliminar(Integer id) {
        diagnosticoRepository.deleteById(id);
    }

    public Diagnostico actualizar(Integer id, Diagnostico diagnosticoActualizado) {
        return diagnosticoRepository.findById(id)
                .map(diagnostico -> {
                    diagnostico.setDescripcion(diagnosticoActualizado.getDescripcion());
                    return diagnosticoRepository.save(diagnostico);
                })
                .orElse(null);
    }
}
