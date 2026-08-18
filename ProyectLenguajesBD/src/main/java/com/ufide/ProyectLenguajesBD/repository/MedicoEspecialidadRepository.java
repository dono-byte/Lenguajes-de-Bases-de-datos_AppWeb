package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.MedicoEspecialidad;

/**
 * Repository de MedicoEspecialidad.
 */
public interface MedicoEspecialidadRepository extends JpaRepository<MedicoEspecialidad, Integer> {
}
