package com.ufide.ProyectLenguajesBD.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufide.ProyectLenguajesBD.entity.Login;

/**
 * Repository de Login.
 *
 * Extiende JpaRepository<Entidad, TipoDelId> y obtiene gratis:
 *   findAll, findById, save, deleteById, count, existsById, etc.
 *
 * Spring genera la implementacion automaticamente al arrancar.
 * No hay que escribir codigo - solo declarar la interface.
 */
public interface LoginRepository extends JpaRepository<Login, Long> {
    
    // Spring crea la consulta automáticamente: SELECT * FROM Login WHERE usuario = ?
    List<Login>findByUsuario(String usuario);
    
}