package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class Rol {

    private Integer pkRol;
    private String nombreRol;
    private String descripcion;
    private List<Usuario> usuarios;

    public Rol() {
    }

    public Rol(String nombreRol, String descripcion) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getPkRol() {
        return pkRol;
    }

    public void setPkRol(Integer pkRol) {
        this.pkRol = pkRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}