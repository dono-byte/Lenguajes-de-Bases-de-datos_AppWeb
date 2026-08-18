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
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CITA")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CITA")
    private Integer pkCita;

    @ManyToOne
    @JoinColumn(name = "FK_PACIENTE", nullable = false, foreignKey = @ForeignKey(name = "FK_CITA_PACIENTE"))
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "FK_CONSULTORIO", nullable = false, foreignKey = @ForeignKey(name = "FK_CITA_CONSULTORIO"))
    private Consultorio consultorio;

    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "DURACION", nullable = true, length = 20)
    private String duracion;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @OneToMany(mappedBy = "cita")
    private List<Consulta> consultas;

    // Constructores
    public Cita() {}

    public Cita(Paciente paciente, Consultorio consultorio, LocalDateTime fechaHora, String duracion, String estado) {
        this.paciente = paciente;
        this.consultorio = consultorio;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getPkCita() {
        return pkCita;
    }

    public void setPkCita(Integer pkCita) {
        this.pkCita = pkCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
