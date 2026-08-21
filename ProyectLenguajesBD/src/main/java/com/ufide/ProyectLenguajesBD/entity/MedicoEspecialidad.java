package com.ufide.ProyectLenguajesBD.entity;

public class MedicoEspecialidad {

    private Integer pkMedicoEspecialidad;
    private PersonalMedico personalMedico;
    private Especialidad especialidad;

    public MedicoEspecialidad() {
    }

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