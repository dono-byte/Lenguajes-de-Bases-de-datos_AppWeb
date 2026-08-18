package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Consulta;
import java.util.List;

/**
 * Repository de Consulta.
 */
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    List<Consulta> findByExpedienteId(Integer expedienteId);
    List<Consulta> findByPersonalMedicoId(Integer personalMedicoId);
}
