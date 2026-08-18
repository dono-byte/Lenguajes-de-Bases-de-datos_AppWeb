package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.repository.DetalleRecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleRecetaService {
    
    @Autowired
    private DetalleRecetaRepository detalleRecetaRepository;

    public List<DetalleReceta> obtenerTodos() {
        return detalleRecetaRepository.findAll();
    }

    public Optional<DetalleReceta> obtenerPorId(Integer id) {
        return detalleRecetaRepository.findById(id);
    }

    public List<DetalleReceta> obtenerPorReceta(Integer recetaId) {
        return detalleRecetaRepository.findByRecetaId(recetaId);
    }

    public DetalleReceta guardar(DetalleReceta detalleReceta) {
        return detalleRecetaRepository.save(detalleReceta);
    }

    public void eliminar(Integer id) {
        detalleRecetaRepository.deleteById(id);
    }

    public DetalleReceta actualizar(Integer id, DetalleReceta detalleActualizado) {
        return detalleRecetaRepository.findById(id)
                .map(detalle -> {
                    detalle.setDosis(detalleActualizado.getDosis());
                    detalle.setFrecuencia(detalleActualizado.getFrecuencia());
                    return detalleRecetaRepository.save(detalle);
                })
                .orElse(null);
    }
}
