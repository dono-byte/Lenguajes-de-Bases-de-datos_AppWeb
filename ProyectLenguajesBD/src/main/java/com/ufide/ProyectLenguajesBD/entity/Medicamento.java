package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "MEDICAMENTOS")
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_MEDICAMENTO")
    private Integer pkMedicamento;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "medicamento")
    private List<DetalleMedicamento> detallesMedicamentos;

    @OneToMany(mappedBy = "medicamento")
    private List<DetalleReceta> detalleRecetas;

    // Constructores
    public Medicamento() {}

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
