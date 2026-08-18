package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Especialidad;

/**
 * Repository de Especialidad.
 */
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}
