package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class Medicamento {

    private Integer pkMedicamento;
    private String nombre;
    private List<DetalleMedicamento> detallesMedicamentos;
    private List<DetalleReceta> detalleRecetas;

    public Medicamento() {
    }

    public Medicamento(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getPkMedicamento() {
        return pkMedicamento;
    }

    public void setPkMedicamento(Integer pkMedicamento) {
        this.pkMedicamento = pkMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DetalleMedicamento> getDetallesMedicamentos() {
        return detallesMedicamentos;
    }

    public void setDetallesMedicamentos(List<DetalleMedicamento> detallesMedicamentos) {
        this.detallesMedicamentos = detallesMedicamentos;
    }

    public List<DetalleReceta> getDetalleRecetas() {
        return detalleRecetas;
    }

    public void setDetalleRecetas(List<DetalleReceta> detalleRecetas) {
        this.detalleRecetas = detalleRecetas;
    }
}