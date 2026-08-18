package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import java.util.List;

/**
 * Repository de DetalleReceta.
 */
public interface DetalleRecetaRepository extends JpaRepository<DetalleReceta, Integer> {
    List<DetalleReceta> findByRecetaPkReceta(Integer recetaId);
}
