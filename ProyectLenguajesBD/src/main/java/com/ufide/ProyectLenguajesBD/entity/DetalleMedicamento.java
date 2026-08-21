package com.ufide.ProyectLenguajesBD.entity;

import java.time.LocalDate;

public class DetalleMedicamento {

    private Integer pkDetalleMedicamento;
    private Medicamento medicamento;
    private String presentacion;
    private String concentracion;
    private Integer entradas;
    private Integer salidas;
    private String lotes;
    private LocalDate vencimientos;

    public DetalleMedicamento() {
    }

    public DetalleMedicamento(Medicamento medicamento, String presentacion, String concentracion,
                              Integer entradas, Integer salidas, String lotes, LocalDate vencimientos) {
        this.medicamento = medicamento;
        this.presentacion = presentacion;
        this.concentracion = concentracion;
        this.entradas = entradas;
        this.salidas = salidas;
        this.lotes = lotes;
        this.vencimientos = vencimientos;
    }

    // Getters y Setters
    public Integer getPkDetalleMedicamento() {
        return pkDetalleMedicamento;
    }

    public void setPkDetalleMedicamento(Integer pkDetalleMedicamento) {
        this.pkDetalleMedicamento = pkDetalleMedicamento;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public Integer getEntradas() {
        return entradas;
    }

    public void setEntradas(Integer entradas) {
        this.entradas = entradas;
    }

    public Integer getSalidas() {
        return salidas;
    }

    public void setSalidas(Integer salidas) {
        this.salidas = salidas;
    }

    public String getLotes() {
        return lotes;
    }

    public void setLotes(String lotes) {
        this.lotes = lotes;
    }

    public LocalDate getVencimientos() {
        return vencimientos;
    }

    public void setVencimientos(LocalDate vencimientos) {
        this.vencimientos = vencimientos;
    }
}