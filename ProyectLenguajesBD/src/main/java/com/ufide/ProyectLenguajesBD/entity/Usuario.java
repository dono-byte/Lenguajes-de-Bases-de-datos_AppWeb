package com.ufide.ProyectLenguajesBD.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class Usuario implements UserDetails {

    private Integer pkUsuario;
    private String usuario;
    private String contrasena;
    private String estado;
    private Rol rol; // referencia a Rol (se llena manualmente)
    private List<PersonalMedico> personalMedicos;

    public Usuario() {
    }

    public Usuario(String usuario, String contrasena, String estado) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (rol != null) ? rol.getNombreRol() : "USER";
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }

    // Getters y Setters
    public Integer getPkUsuario() {
        return pkUsuario;
    }

    public void setPkUsuario(Integer pkUsuario) {
        this.pkUsuario = pkUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<PersonalMedico> getPersonalMedicos() {
        return personalMedicos;
    }

    public void setPersonalMedicos(List<PersonalMedico> personalMedicos) {
        this.personalMedicos = personalMedicos;
    }
}