package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Medicamento;
import java.util.Optional;

/**
 * Repository de Medicamento.
 */
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {
    Optional<Medicamento> findByNombre(String nombre);
}
