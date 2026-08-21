package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDate;
import java.util.List;

public class Paciente {

    private Integer pkPaciente;
    private String cedula;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String direccion;
    private List<Cita> citas;
    private Expediente expediente;

    public Paciente() {
    }

    public Paciente(String cedula, String nombre, LocalDate fechaNacimiento, String genero,
                    String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Integer getPkPaciente() {
        return pkPaciente;
    }

    public void setPkPaciente(Integer pkPaciente) {
        this.pkPaciente = pkPaciente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }
}