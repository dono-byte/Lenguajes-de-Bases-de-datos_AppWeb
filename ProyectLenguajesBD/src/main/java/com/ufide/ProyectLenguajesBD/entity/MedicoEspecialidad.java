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
@Table(name = "MEDICO_ESPECIALIDAD")
public class MedicoEspecialidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_MEDICO_ESPECIALIDAD")
    private Integer pkMedicoEspecialidad;

    @ManyToOne
    @JoinColumn(name = "FK_PERSONAL_MEDICO", nullable = false, foreignKey = @ForeignKey(name = "FK_MEDICO_ESP_PERSONAL"))
    private PersonalMedico personalMedico;

    @ManyToOne
    @JoinColumn(name = "FK_ESPECIALIDAD", nullable = false, foreignKey = @ForeignKey(name = "FK_MEDICO_ESP_ESPECIALIDAD"))
    private Especialidad especialidad;

    // Constructores
    public MedicoEspecialidad() {}

    public MedicoEspecialidad(PersonalMedico personalMedico, Especialidad especialidad) {
        this.personalMedico = personalMedico;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Integer getPkMedicoEspecialidad() {
        return pkMedicoEspecialidad;
    }

    public void setPkMedicoEspecialidad(Integer pkMedicoEspecialidad) {
        this.pkMedicoEspecialidad = pkMedicoEspecialidad;
    }

    public PersonalMedico getPersonalMedico() {
        return personalMedico;
    }

    public void setPersonalMedico(PersonalMedico personalMedico) {
        this.personalMedico = personalMedico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
}
