package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class Consultorio {

    private Integer pkConsultorio;
    private String numeroConsultorio;
    private String localidad;
    private String provincia;
    private List<Cita> citas;

    public Consultorio() {
    }

    public Consultorio(String numeroConsultorio, String localidad, String provincia) {
        this.numeroConsultorio = numeroConsultorio;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    // Getters y Setters
    public Integer getPkConsultorio() {
        return pkConsultorio;
    }

    public void setPkConsultorio(Integer pkConsultorio) {
        this.pkConsultorio = pkConsultorio;
    }

    public String getNumeroConsultorio() {
        return numeroConsultorio;
    }

    public void setNumeroConsultorio(String numeroConsultorio) {
        this.numeroConsultorio = numeroConsultorio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}