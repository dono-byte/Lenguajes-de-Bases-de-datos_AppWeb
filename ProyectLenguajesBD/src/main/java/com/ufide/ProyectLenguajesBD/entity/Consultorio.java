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
@Table(name = "CONSULTORIO")
public class Consultorio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CONSULTORIO")
    private Integer pkConsultorio;

    @Column(name = "NUMERO_CONSULTORIO", nullable = false, length = 10)
    private String numeroConsultorio;

    @Column(name = "LOCALIDAD", nullable = true, length = 50)
    private String localidad;

    @Column(name = "PROVINCIA", nullable = true, length = 50)
    private String provincia;

    @OneToMany(mappedBy = "consultorio")
    private List<Cita> citas;

    // Constructores
    public Consultorio() {}

    public Consultorio(String numeroConsultorio, String localidad, String provincia) {
        this.numeroConsultorio = numeroConsultorio;
        this.localidad = localidad;
        this.provincia = provincia;
    }

    // Getters y Setters
    public Integer getPkConsultorio() {
        return pkConsultorio;
    }

    public void setPkConsultorio(Integer pkConsultorio) {
        this.pkConsultorio = pkConsultorio;
    }

    public String getNumeroConsultorio() {
        return numeroConsultorio;
    }

    public void setNumeroConsultorio(String numeroConsultorio) {
        this.numeroConsultorio = numeroConsultorio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
}
