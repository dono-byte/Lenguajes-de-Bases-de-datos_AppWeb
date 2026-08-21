package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.repository.DetalleRecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        return detalleRecetaRepository.findByRecetaPkReceta(recetaId);
    }

    public DetalleReceta guardar(DetalleReceta detalleReceta) {
        return detalleRecetaRepository.save(detalleReceta);
    }

    public DetalleReceta actualizar(Integer id, DetalleReceta detalleActualizado) {
        return detalleRecetaRepository.findById(id)
                .map(detalle -> {
                    detalle.setReceta(detalleActualizado.getReceta());
                    detalle.setMedicamento(detalleActualizado.getMedicamento());
                    detalle.setDosis(detalleActualizado.getDosis());
                    detalle.setFrecuencia(detalleActualizado.getFrecuencia());
                    return detalleRecetaRepository.save(detalle);
                })
                .orElseThrow(() -> new RuntimeException("Detalle de receta no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        detalleRecetaRepository.deleteById(id);
    }
}