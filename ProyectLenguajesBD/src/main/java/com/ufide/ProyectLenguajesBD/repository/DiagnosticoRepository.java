package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Diagnostico;
import java.util.Optional;

/**
 * Repository de Diagnostico.
 */
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {
    Optional<Diagnostico> findByCodigoCie10(String codigoCie10);
}
