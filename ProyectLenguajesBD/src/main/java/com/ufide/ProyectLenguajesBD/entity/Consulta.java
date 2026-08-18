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
@Table(name = "CONSULTA")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CONSULTA")
    private Integer pkConsulta;

    @ManyToOne
    @JoinColumn(name = "FK_EXPEDIENTE", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULTA_EXPEDIENTE"))
    private Expediente expediente;

    @ManyToOne
    @JoinColumn(name = "FK_PERSONAL_MEDICO", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULTA_PERSONAL"))
    private PersonalMedico personalMedico;

    @ManyToOne
    @JoinColumn(name = "FK_DIAGNOSTICO", nullable = true, foreignKey = @ForeignKey(name = "FK_CONSULTA_DIAGNOSTICO"))
    private Diagnostico diagnostico;

    @ManyToOne
    @JoinColumn(name = "FK_CITA", nullable = true, foreignKey = @ForeignKey(name = "FK_CONSULTA_CITA"))
    private Cita cita;

    @Column(name = "FECHA_CONSULTA", nullable = false)
    private LocalDate fechaConsulta;

    @Column(name = "MOTIVO", nullable = true, length = 200)
    private String motivo;

    @Column(name = "OBSERVACIONES", nullable = true, length = 200)
    private String observaciones;

    @OneToMany(mappedBy = "consulta")
    private List<Receta> recetas;

    // Constructores
    public Consulta() {}

    public Consulta(Expediente expediente, PersonalMedico personalMedico, Diagnostico diagnostico,
                   Cita cita, LocalDate fechaConsulta, String motivo, String observaciones) {
        this.expediente = expediente;
        this.personalMedico = personalMedico;
        this.diagnostico = diagnostico;
        this.cita = cita;
        this.fechaConsulta = fechaConsulta;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Integer getPkConsulta() {
        return pkConsulta;
    }

    public void setPkConsulta(Integer pkConsulta) {
        this.pkConsulta = pkConsulta;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public PersonalMedico getPersonalMedico() {
        return personalMedico;
    }

    public void setPersonalMedico(PersonalMedico personalMedico) {
        this.personalMedico = personalMedico;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDate fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}
