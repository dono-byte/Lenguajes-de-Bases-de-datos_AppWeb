package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ESPECIALIDAD")
public class Especialidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_ESPECIALIDAD")
    private Integer pkEspecialidad;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "DESCRIPCION", nullable = true, length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "especialidad")
    private List<MedicoEspecialidad> medicoEspecialidades;

    // Constructores
    public Especialidad() {}

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
