package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;
import java.util.List;

/**
 * Repository de DetalleMedicamento.
 */
public interface DetalleMedicamentoRepository extends JpaRepository<DetalleMedicamento, Integer> {
    List<DetalleMedicamento> findByMedicamentoPkMedicamento(Integer medicamentoId);
}
