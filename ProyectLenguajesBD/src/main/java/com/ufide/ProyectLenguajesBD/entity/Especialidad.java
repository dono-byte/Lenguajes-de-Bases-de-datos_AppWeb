package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class Especialidad {

    private Integer pkEspecialidad;
    private String nombre;
    private String descripcion;
    private List<MedicoEspecialidad> medicoEspecialidades;

    public Especialidad() {
    }

    public Especialidad(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getPkEspecialidad() {
        return pkEspecialidad;
    }

    public void setPkEspecialidad(Integer pkEspecialidad) {
        this.pkEspecialidad = pkEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<MedicoEspecialidad> getMedicoEspecialidades() {
        return medicoEspecialidades;
    }

    public void setMedicoEspecialidades(List<MedicoEspecialidad> medicoEspecialidades) {
        this.medicoEspecialidades = medicoEspecialidades;
    }
}