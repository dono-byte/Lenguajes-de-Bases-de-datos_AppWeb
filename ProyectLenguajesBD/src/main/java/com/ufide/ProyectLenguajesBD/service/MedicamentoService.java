package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Medicamento;
import com.ufide.ProyectLenguajesBD.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {
    
    @Autowired
    private MedicamentoRepository medicamentoRepository;

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

    public void eliminar(Integer id) {
        medicamentoRepository.deleteById(id);
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
