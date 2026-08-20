package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Medicamento;
import com.ufide.ProyectLenguajesBD.repository.MedicamentoRepository;
import com.ufide.ProyectLenguajesBD.repository.DetalleMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;

@Service
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

    @Transactional
    public void eliminar(Integer id) {
        detalleMedicamentoRepository.deleteAll(detalleMedicamentoRepository.findByMedicamentoPkMedicamento(id));
        medicamentoRepository.deleteById(id);
    }

    @Transactional
    public Medicamento guardarConDetalle(Medicamento medicamento, DetalleMedicamento detalle) {
        Medicamento guardado = medicamentoRepository.save(medicamento);
        detalle.setMedicamento(guardado);
        detalleMedicamentoRepository.save(detalle);
        return guardado;
    }

    @Transactional
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

    public Medicamento actualizar(Integer id, Medicamento medicamentoActualizado) {
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setNombre(medicamentoActualizado.getNombre());
                    return medicamentoRepository.save(medicamento);
                })
                .orElse(null);
    }
}
