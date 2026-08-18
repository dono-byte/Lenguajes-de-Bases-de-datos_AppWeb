package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Receta;
import com.ufide.ProyectLenguajesBD.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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
        return recetaRepository.findByConsultaId(consultaId);
    }

    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    public void eliminar(Integer id) {
        recetaRepository.deleteById(id);
    }
}
