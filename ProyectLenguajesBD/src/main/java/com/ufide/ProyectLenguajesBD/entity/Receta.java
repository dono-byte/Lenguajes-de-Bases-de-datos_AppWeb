package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDate;
import java.util.List;

public class Receta {

    private Integer pkReceta;
    private Consulta consulta;
    private LocalDate fechaEmision;
    private List<DetalleReceta> detalleRecetas;

    public Receta() {
    }

    public Receta(Consulta consulta, LocalDate fechaEmision) {
        this.consulta = consulta;
        this.fechaEmision = fechaEmision;
    }

    // Getters y Setters
    public Integer getPkReceta() {
        return pkReceta;
    }

    public void setPkReceta(Integer pkReceta) {
        this.pkReceta = pkReceta;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<DetalleReceta> getDetalleRecetas() {
        return detalleRecetas;
    }

    public void setDetalleRecetas(List<DetalleReceta> detalleRecetas) {
        this.detalleRecetas = detalleRecetas;
    }
}