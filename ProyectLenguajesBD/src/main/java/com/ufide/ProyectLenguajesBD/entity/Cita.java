package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Cita {

    private Integer pkCita;
    private Paciente paciente;
    private Consultorio consultorio;
    private LocalDateTime fechaHora;
    private String duracion;
    private String estado;
    private List<Consulta> consultas;

    public Cita() {
    }

    public Cita(Paciente paciente, Consultorio consultorio, LocalDateTime fechaHora,
                String duracion, String estado) {
        this.paciente = paciente;
        this.consultorio = consultorio;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getPkCita() {
        return pkCita;
    }

    public void setPkCita(Integer pkCita) {
        this.pkCita = pkCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}