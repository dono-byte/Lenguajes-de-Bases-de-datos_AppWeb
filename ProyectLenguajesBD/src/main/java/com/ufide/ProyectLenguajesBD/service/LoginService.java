package com.ufide.ProyectLenguajesBD.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufide.ProyectLenguajesBD.entity.Login;
import com.ufide.ProyectLenguajesBD.repository.LoginRepository;

/**
 * Capa de logica de negocio.
 *
 * Coordina las operaciones sobre Login.
 * El Controller llama a este Service, NUNCA al Repository directamente.
 */@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepo;

    /**
     * REGISTRO
     */
    public Login registrarUsuario(Login nuevoUsuario) {
    /**
     * REGLAS
     */
        // Regla de negocio 1: Verificar que el nombre de usuario no esté tomado
        List<Login> usuarioExistente = loginRepo.findByUsuario(nuevoUsuario.getUsuario());
        if (!usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }

        // Regla de negocio 2: Validar tamaño mínimo de contraseña
        if (nuevoUsuario.getContrasena() == null || nuevoUsuario.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }

        // Regla de negocio 3: Todo usuario nuevo empieza con estado "ACTIVO"
        nuevoUsuario.setEstado("ACTIVO");

        return loginRepo.save(nuevoUsuario);
    }

    /**
     * Verificacion al inicar sesion
     */
    public boolean iniciarSesion(String usuario, String contrasenaPlana) {

        List<Login> loginOpt = loginRepo.findByUsuario(usuario);

        if (loginOpt.isEmpty()) {
            return false; 
        }

        Login usuarioEncontrado = loginOpt.get(0);

        // usuario  "INACTIVO" o "BLOQUEADO", no puede entrar
        if (!"ACTIVO".equalsIgnoreCase(usuarioEncontrado.getEstado())) {
            throw new IllegalStateException("Tu cuenta no está activa. Contacta al administrador.");
        }

        // vericacion de la contraseña
        return usuarioEncontrado.getContrasena().equals(contrasenaPlana);
    }
}