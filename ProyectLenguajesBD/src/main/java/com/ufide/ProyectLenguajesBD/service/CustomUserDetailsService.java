package com.ufide.ProyectLenguajesBD.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ufide.ProyectLenguajesBD.entity.Rol;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
import com.ufide.ProyectLenguajesBD.repository.RolRepository;
import com.ufide.ProyectLenguajesBD.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository; 

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (usuario.getRol() != null && usuario.getRol().getPkRol() != null) {
            Rol rolCompleto = rolRepository.findById(usuario.getRol().getPkRol()).orElse(null);
            usuario.setRol(rolCompleto);
        }
        return usuario;
    }
}