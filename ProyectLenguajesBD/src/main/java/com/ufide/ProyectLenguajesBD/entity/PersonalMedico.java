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
import java.util.List;

@Entity
@Table(name = "PERSONAL_MEDICO")
public class PersonalMedico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PERSONAL_MEDICO")
    private Integer pkPersonalMedico;

    @ManyToOne
    @JoinColumn(name = "FK_USUARIO", nullable = false, foreignKey = @ForeignKey(name = "FK_PERSONAL_MEDICO_USUARIO"))
    private Usuario usuario;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Column(name = "SEG_APELLIDO", nullable = true, length = 100)
    private String segApellido;

    @Column(name = "CODIGO_MEDICO", nullable = false, unique = true, length = 30)
    private String codigoMedico;

    @Column(name = "CORREO_ELECTRONICO", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    @Column(name = "TELEFONO", nullable = true, length = 20)
    private String telefono;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @OneToMany(mappedBy = "personalMedico")
    private List<MedicoEspecialidad> medicoEspecialidades;

    @OneToMany(mappedBy = "personalMedico")
    private List<Consulta> consultas;

    // Constructores
    public PersonalMedico() {}

    public PersonalMedico(Usuario usuario, String nombre, String apellido, String segApellido,
                         String codigoMedico, String correoElectronico, String telefono, String estado) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.segApellido = segApellido;
        this.codigoMedico = codigoMedico;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getPkPersonalMedico() {
        return pkPersonalMedico;
    }

    public void setPkPersonalMedico(Integer pkPersonalMedico) {
        this.pkPersonalMedico = pkPersonalMedico;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSegApellido() {
        return segApellido;
    }

    public void setSegApellido(String segApellido) {
        this.segApellido = segApellido;
    }

    public String getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(String codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<MedicoEspecialidad> getMedicoEspecialidades() {
        return medicoEspecialidades;
    }

    public void setMedicoEspecialidades(List<MedicoEspecialidad> medicoEspecialidades) {
        this.medicoEspecialidades = medicoEspecialidades;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
