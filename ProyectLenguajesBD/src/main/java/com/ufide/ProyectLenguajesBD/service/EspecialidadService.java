package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Especialidad;
import com.ufide.ProyectLenguajesBD.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {
    
    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> obtenerTodos() {
        return especialidadRepository.findAll();
    }

    public Optional<Especialidad> obtenerPorId(Integer id) {
        return especialidadRepository.findById(id);
    }

    public Especialidad guardar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    public void eliminar(Integer id) {
        especialidadRepository.deleteById(id);
    }

    public Especialidad actualizar(Integer id, Especialidad especialidadActualizada) {
        return especialidadRepository.findById(id)
                .map(especialidad -> {
                    especialidad.setNombre(especialidadActualizada.getNombre());
                    especialidad.setDescripcion(especialidadActualizada.getDescripcion());
                    return especialidadRepository.save(especialidad);
                })
                .orElse(null);
    }
}
