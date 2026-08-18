package com.ufide.ProyectLenguajesBD.service;

import com.ufide.ProyectLenguajesBD.entity.Usuario;
import com.ufide.ProyectLenguajesBD.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public boolean validarCredenciales(String usuario, String contraseña) {
        Optional<Usuario> u = usuarioRepository.findByUsuario(usuario);
        return u.isPresent() && u.get().getContraseña().equals(contraseña) && "Activo".equals(u.get().getEstado());
    }
}
