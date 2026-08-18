package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import java.util.Optional;

/**
 * Repository de Expediente.
 */
public interface ExpedienteRepository extends JpaRepository<Expediente, Integer> {
    Optional<Expediente> findByPacienteId(Integer pacienteId);
}
