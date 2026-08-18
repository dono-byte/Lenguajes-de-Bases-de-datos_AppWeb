package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "EXPEDIENTE")
public class Expediente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_EXPEDIENTE")
    private Integer pkExpediente;

    @OneToOne
    @JoinColumn(name = "FK_PACIENTE", nullable = false, unique = true, foreignKey = @ForeignKey(name = "FK_EXPEDIENTE_PACIENTE"))
    private Paciente paciente;

    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "expediente")
    private List<Consulta> consultas;

    // Constructores
    public Expediente() {}

    public Expediente(Paciente paciente, LocalDate fechaCreacion) {
        this.paciente = paciente;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Integer getPkExpediente() {
        return pkExpediente;
    }

    public void setPkExpediente(Integer pkExpediente) {
        this.pkExpediente = pkExpediente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
