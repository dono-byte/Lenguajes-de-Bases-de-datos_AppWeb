package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;
import com.ufide.ProyectLenguajesBD.entity.Medicamento;
import com.ufide.ProyectLenguajesBD.repository.DetalleMedicamentoRepository;
import com.ufide.ProyectLenguajesBD.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private DetalleMedicamentoRepository detalleMedicamentoRepository;

    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> obtenerPorId(Integer id) {
        return medicamentoRepository.findById(id);
    }

    public Optional<Medicamento> obtenerPorNombre(String nombre) {
        return medicamentoRepository.findByNombre(nombre);
    }

    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public Medicamento actualizar(Integer id, Medicamento medicamentoActualizado) {
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setNombre(medicamentoActualizado.getNombre());
                    return medicamentoRepository.save(medicamento);
                })
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        // Eliminar primero los detalles asociados (cascada manual)
        List<DetalleMedicamento> detalles = detalleMedicamentoRepository.findByMedicamentoPkMedicamento(id);
        for (DetalleMedicamento detalle : detalles) {
            detalleMedicamentoRepository.deleteById(detalle.getPkDetalleMedicamento());
        }
        medicamentoRepository.deleteById(id);
    }

    // Método que guarda medicamento junto con su detalle
    public Medicamento guardarConDetalle(Medicamento medicamento, DetalleMedicamento detalle) {
        Medicamento guardado = medicamentoRepository.save(medicamento);
        detalle.setMedicamento(guardado);
        detalleMedicamentoRepository.save(detalle);
        return guardado;
    }

    // Método que actualiza medicamento y su detalle (suponiendo un solo detalle por medicamento)
    public Optional<Medicamento> actualizarConDetalle(Integer id, Medicamento medicamentoActualizado,
                                                      DetalleMedicamento detalleActualizado) {
        return medicamentoRepository.findById(id).map(medicamento -> {
            medicamento.setNombre(medicamentoActualizado.getNombre());
            medicamentoRepository.save(medicamento);

            List<DetalleMedicamento> detalles = detalleMedicamentoRepository.findByMedicamentoPkMedicamento(id);
            DetalleMedicamento detalle = detalles.isEmpty() ? new DetalleMedicamento() : detalles.get(0);
            detalle.setMedicamento(medicamento);
            detalle.setPresentacion(detalleActualizado.getPresentacion());
            detalle.setConcentracion(detalleActualizado.getConcentracion());
            detalle.setEntradas(detalleActualizado.getEntradas());
            detalle.setSalidas(detalleActualizado.getSalidas());
            detalle.setLotes(detalleActualizado.getLotes());
            detalle.setVencimientos(detalleActualizado.getVencimientos());
            detalleMedicamentoRepository.save(detalle);
            return medicamento;
        });
    }
}