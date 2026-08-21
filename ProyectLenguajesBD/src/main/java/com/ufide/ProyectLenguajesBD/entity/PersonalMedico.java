package com.ufide.ProyectLenguajesBD.entity;

import java.util.List;

public class PersonalMedico {

    private Integer pkPersonalMedico;
    private Usuario usuario; // referencia
    private String nombre;
    private String apellido;
    private String segApellido;
    private String codigoMedico;
    private String correoElectronico;
    private String telefono;
    private String estado;
    private List<MedicoEspecialidad> medicoEspecialidades;
    private List<Consulta> consultas;

    public PersonalMedico() {
    }

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