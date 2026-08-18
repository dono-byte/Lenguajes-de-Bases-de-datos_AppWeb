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
@Table(name = "DIAGNOSTICO")
public class Diagnostico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_DIAGNOSTICO")
    private Integer pkDiagnostico;

    @Column(name = "CODIGO_CIE10", nullable = false, unique = true, length = 10)
    private String codigoCie10;

    @Column(name = "DESCRIPCION", nullable = false, length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "diagnostico")
    private List<Consulta> consultas;

    // Constructores
    public Diagnostico() {}

    public Diagnostico(String codigoCie10, String descripcion) {
        this.codigoCie10 = codigoCie10;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getPkDiagnostico() {
        return pkDiagnostico;
    }

    public void setPkDiagnostico(Integer pkDiagnostico) {
        this.pkDiagnostico = pkDiagnostico;
    }

    public String getCodigoCie10() {
        return codigoCie10;
    }

    public void setCodigoCie10(String codigoCie10) {
        this.codigoCie10 = codigoCie10;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
