package com.ufide.ProyectLenguajesBD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
import java.util.Optional;

/**
 * Repository de Usuario.
 * Proporciona métodos CRUD y consultas personalizadas.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
}
