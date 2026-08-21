package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Usuario;
import com.ufide.ProyectLenguajesBD.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorNombreUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsuario(usuarioActualizado.getUsuario());
                    usuario.setContrasena(usuarioActualizado.getContrasena());
                    usuario.setEstado(usuarioActualizado.getEstado());
                    usuario.setRol(usuarioActualizado.getRol());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    // Método auxiliar para validación de credenciales (puede usarse en el controlador)
    public boolean validarCredenciales(String usuario, String contrasena) {
        Optional<Usuario> u = usuarioRepository.findByUsuario(usuario);
        return u.isPresent() && u.get().getContrasena().equals(contrasena) && "ACTIVO".equalsIgnoreCase(u.get().getEstado());
    }
}