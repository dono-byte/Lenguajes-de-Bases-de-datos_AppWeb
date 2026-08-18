package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import java.util.Optional;

/**
 * Repository de Paciente.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByCedula(String cedula);
}
