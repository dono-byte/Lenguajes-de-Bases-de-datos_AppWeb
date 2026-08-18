package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RECETAS")
public class Receta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_RECETA")
    private Integer pkReceta;

    @ManyToOne
    @JoinColumn(name = "FK_CONSULTA", nullable = false, foreignKey = @ForeignKey(name = "FK_RECETA_CONSULTA"))
    private Consulta consulta;

    @Column(name = "FECHA_EMISION", nullable = false)
    private LocalDate fechaEmision;

    @OneToMany(mappedBy = "receta")
    private List<DetalleReceta> detalleRecetas;

    // Constructores
    public Receta() {}

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
