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
import java.time.LocalDate;

@Entity
@Table(name = "DETALLE_MEDICAMENTOS")
public class DetalleMedicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_DETALLE_MEDICAMENTOS")
    private Integer pkDetalleMedicamento;

    @ManyToOne
    @JoinColumn(name = "FK_MEDICAMENTO", nullable = false, foreignKey = @ForeignKey(name = "FK_DETALLE_MED_MEDICAMENTO"))
    private Medicamento medicamento;

    @Column(name = "PRESENTACION", nullable = true, length = 100)
    private String presentacion;

    @Column(name = "CONCENTRACION", nullable = true, length = 50)
    private String concentracion;

    @Column(name = "ENTRADAS", nullable = true)
    private Integer entradas;

    @Column(name = "SALIDAS", nullable = true)
    private Integer salidas;

    @Column(name = "LOTES", nullable = true, length = 50)
    private String lotes;

    @Column(name = "VENCIMIENTOS", nullable = true)
    private LocalDate vencimientos;

    // Constructores
    public DetalleMedicamento() {}

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
