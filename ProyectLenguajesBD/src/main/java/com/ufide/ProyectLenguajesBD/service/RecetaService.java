package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Receta;
import com.ufide.ProyectLenguajesBD.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> obtenerTodos() {
        return recetaRepository.findAll();
    }

    public Optional<Receta> obtenerPorId(Integer id) {
        return recetaRepository.findById(id);
    }

    public List<Receta> obtenerPorConsulta(Integer consultaId) {
        return recetaRepository.findByConsultaPkConsulta(consultaId);
    }

    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    public Receta actualizar(Integer id, Receta recetaActualizada) {
        return recetaRepository.findById(id)
                .map(receta -> {
                    receta.setConsulta(recetaActualizada.getConsulta());
                    receta.setFechaEmision(recetaActualizada.getFechaEmision());
                    return recetaRepository.save(receta);
                })
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
    }

    public void eliminar(Integer id) {
        recetaRepository.deleteById(id);
    }
}