package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Consultorio;

/**
 * Repository de Consultorio.
 */
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {
}
