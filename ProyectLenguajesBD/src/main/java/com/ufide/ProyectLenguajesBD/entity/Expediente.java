package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDate;
import java.util.List;

public class Expediente {

    private Integer pkExpediente;
    private Paciente paciente;
    private LocalDate fechaCreacion;
    private List<Consulta> consultas;

    public Expediente() {
    }

    public Expediente(Paciente paciente, LocalDate fechaCreacion) {
        this.paciente = paciente;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Integer getPkExpediente() {
        return pkExpediente;
    }

    public void setPkExpediente(Integer pkExpediente) {
        this.pkExpediente = pkExpediente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}