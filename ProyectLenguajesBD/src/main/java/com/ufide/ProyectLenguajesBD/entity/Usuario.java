package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;

@Entity
@Table(name = "USUARIO")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_USUARIO")
    private Integer pkUsuario;

    @Column(name = "USUARIO", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "CONTRASENA", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "ESTADO", nullable = false, length = 50)
    private String estado;

    @OneToMany(mappedBy = "usuario")
    private List<PersonalMedico> personalMedicos;

    // Constructores
    public Usuario() {}

    public Usuario(String usuario, String contrasena, String estado) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getPkUsuario() {
        return pkUsuario;
    }

    public void setPkUsuario(Integer pkUsuario) {
        this.pkUsuario = pkUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contrasena;
    }

    public void setContraseña(String contraseña) {
        this.contrasena = contraseña;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<PersonalMedico> getPersonalMedicos() {
        return personalMedicos;
    }

    public void setPersonalMedicos(List<PersonalMedico> personalMedicos) {
        this.personalMedicos = personalMedicos;
    }
}
