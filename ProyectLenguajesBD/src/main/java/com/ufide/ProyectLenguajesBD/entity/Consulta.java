package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDate;
import java.util.List;

public class Consulta {

    private Integer pkConsulta;
    private Expediente expediente;
    private PersonalMedico personalMedico;
    private Diagnostico diagnostico;
    private Cita cita;
    private LocalDate fechaConsulta;
    private String motivo;
    private String observaciones;
    private List<Receta> recetas;

    public Consulta() {
    }

    public Consulta(Expediente expediente, PersonalMedico personalMedico, Diagnostico diagnostico,
                    Cita cita, LocalDate fechaConsulta, String motivo, String observaciones) {
        this.expediente = expediente;
        this.personalMedico = personalMedico;
        this.diagnostico = diagnostico;
        this.cita = cita;
        this.fechaConsulta = fechaConsulta;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Integer getPkConsulta() {
        return pkConsulta;
    }

    public void setPkConsulta(Integer pkConsulta) {
        this.pkConsulta = pkConsulta;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public PersonalMedico getPersonalMedico() {
        return personalMedico;
    }

    public void setPersonalMedico(PersonalMedico personalMedico) {
        this.personalMedico = personalMedico;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}