package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PACIENTE")
public class Paciente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PACIENTE")
    private Integer pkPaciente;

    @Column(name = "CEDULA", nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "GENERO", nullable = false, length = 20)
    private String genero;

    @Column(name = "TELEFONO", nullable = true, length = 20)
    private String telefono;

    @Column(name = "DIRECCION", nullable = true, length = 255)
    private String direccion;

    @OneToMany(mappedBy = "paciente")
    private List<Cita> citas;

    @OneToOne(mappedBy = "paciente")
    private Expediente expediente;

    // Constructores
    public Paciente() {}

    public Paciente(String cedula, String nombre, LocalDate fechaNacimiento, String genero, String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Integer getPkPaciente() {
        return pkPaciente;
    }

    public void setPkPaciente(Integer pkPaciente) {
        this.pkPaciente = pkPaciente;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }
}
