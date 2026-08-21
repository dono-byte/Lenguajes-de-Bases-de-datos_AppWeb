package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class Diagnostico {

    private Integer pkDiagnostico;
    private String codigoCie10;
    private String descripcion;
    private List<Consulta> consultas;

    public Diagnostico() {
    }

    public Diagnostico(String codigoCie10, String descripcion) {
        this.codigoCie10 = codigoCie10;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getPkDiagnostico() {
        return pkDiagnostico;
    }

    public void setPkDiagnostico(Integer pkDiagnostico) {
        this.pkDiagnostico = pkDiagnostico;
    }

    public String getCodigoCie10() {
        return codigoCie10;
    }

    public void setCodigoCie10(String codigoCie10) {
        this.codigoCie10 = codigoCie10;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}