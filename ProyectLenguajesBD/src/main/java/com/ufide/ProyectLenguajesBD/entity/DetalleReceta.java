package com.ufide.ProyectLenguajesBD.entity;

public class DetalleReceta {

    private Integer pkDetalleReceta;
    private Receta receta;
    private Medicamento medicamento;
    private String dosis;
    private String frecuencia;

    public DetalleReceta() {
    }

    public DetalleReceta(Receta receta, Medicamento medicamento, String dosis, String frecuencia) {
        this.receta = receta;
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
    }

    // Getters y Setters
    public Integer getPkDetalleReceta() {
        return pkDetalleReceta;
    }

    public void setPkDetalleReceta(Integer pkDetalleReceta) {
        this.pkDetalleReceta = pkDetalleReceta;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }
}