package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Receta;
import java.util.List;

/**
 * Repository de Receta.
 */
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    List<Receta> findByConsultaPkConsulta(Integer consultaId);
}
