package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;
import com.ufide.ProyectLenguajesBD.repository.DetalleMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleMedicamentoService {

    @Autowired
    private DetalleMedicamentoRepository detalleMedicamentoRepository;

    public List<DetalleMedicamento> obtenerTodos() {
        return detalleMedicamentoRepository.findAll();
    }

    public Optional<DetalleMedicamento> obtenerPorId(Integer id) {
        return detalleMedicamentoRepository.findById(id);
    }

    public List<DetalleMedicamento> obtenerPorMedicamento(Integer medicamentoId) {
        return detalleMedicamentoRepository.findByMedicamentoPkMedicamento(medicamentoId);
    }

    public DetalleMedicamento guardar(DetalleMedicamento detalleMedicamento) {
        return detalleMedicamentoRepository.save(detalleMedicamento);
    }

    public DetalleMedicamento actualizar(Integer id, DetalleMedicamento detalleActualizado) {
        return detalleMedicamentoRepository.findById(id)
                .map(detalle -> {
                    detalle.setMedicamento(detalleActualizado.getMedicamento());
                    detalle.setPresentacion(detalleActualizado.getPresentacion());
                    detalle.setConcentracion(detalleActualizado.getConcentracion());
                    detalle.setEntradas(detalleActualizado.getEntradas());
                    detalle.setSalidas(detalleActualizado.getSalidas());
                    detalle.setLotes(detalleActualizado.getLotes());
                    detalle.setVencimientos(detalleActualizado.getVencimientos());
                    return detalleMedicamentoRepository.save(detalle);
                })
                .orElseThrow(() -> new RuntimeException("Detalle de medicamento no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        detalleMedicamentoRepository.deleteById(id);
    }
}