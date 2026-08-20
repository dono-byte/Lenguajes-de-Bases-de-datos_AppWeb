package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;

@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

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

    @ManyToOne
    @JoinColumn(name = "FK_ROL", foreignKey = @ForeignKey(name = "FK_USUARIO_ROL"))
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    private List<PersonalMedico> personalMedicos;

    // Constructores
    public Usuario() {
    }

    public Usuario(String usuario, String contrasena, String estado) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // El nombre del rol debe estar en mayúsculas y con prefijo "ROLE_"
        String roleName = rol != null ? rol.getNombreRol() : "USER";
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return "ACTIVO".equalsIgnoreCase(estado);
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
