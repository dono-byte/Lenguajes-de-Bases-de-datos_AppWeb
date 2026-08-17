package com.ufide.ProyectLenguajesBD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad Login - representa la tabla Logins en MySQL.
 *
 * Anotaciones JPA:
 *   @Entity        -> marca la clase como tabla
 *   @Table         -> nombre custom de la tabla
 *   @Id            -> llave primaria
 *   @GeneratedValue -> auto-incremento
 *   @Column        -> configuracion de la columna
 */
@Entity
@Table(name = "usuario")
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_USUARIO")
    private Long pk_usuario;

    @Column(name = "USUARIO", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "CONTRASENA", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "ESTADO", nullable = false, length = 50)
    private String estado;

  /**    pk_usuario BIGINT NOT NULL AUTO_INCREMENT,
    usuario VARCHAR(50) UNIQUE,
    contrasena VARCHAR(100),
    estado VARCHAR(50),
> Las más usadas: `@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Max`, `@Positive`, `@PositiveOrZero`, `@Future`, `@FutureOrPresent`, `@Email`, `@Pattern`.*/

    /** Constructor vacio - obligatorio para JPA. */
    public Login() {}

    public Login(String usuario, String contrasena, String estado) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Getters y setters

    public Long getPk_usuario() { return pk_usuario; }
    public void setPk_usuario(Long pk_usuario) { this.pk_usuario = pk_usuario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    /** Util: marca si hay poco stock. 
    public boolean isBajoStock() {
        return stock > 0 && stock < 5;
    }

    /** Util: marca si esta agotado. 
    public boolean isAgotado() {
        return stock == 0;
    }*/
}
