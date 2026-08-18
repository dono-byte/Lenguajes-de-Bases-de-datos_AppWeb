package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Cita;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository de Cita.
 */
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByPacientePkPaciente(Integer pkPaciente);
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
