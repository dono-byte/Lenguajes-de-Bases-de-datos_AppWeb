package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DETALLE_RECETA")
public class DetalleReceta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_DETALLE_RECETA")
    private Integer pkDetalleReceta;

    @ManyToOne
    @JoinColumn(name = "FK_RECETA", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_RECETA_RECETA"))
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "FK_MEDICAMENTOS", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_RECETA_MEDICAMENTO"))
    private Medicamento medicamento;

    @Column(name = "DOSIS", nullable = true, length = 50)
    private String dosis;

    @Column(name = "FRECUENCIA", nullable = true, length = 50)
    private String frecuencia;

    // Constructores
    public DetalleReceta() {}

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
