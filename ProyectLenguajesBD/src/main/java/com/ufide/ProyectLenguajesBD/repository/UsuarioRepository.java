package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Rol;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getUsuarioCall;
    private final SimpleJdbcCall insUsuarioCall;
    private final SimpleJdbcCall updUsuarioCall;
    private final SimpleJdbcCall delUsuarioCall;
    private final SimpleJdbcCall getUsuarioPorNombreCall;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.getUsuarioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_USUARIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, new UsuarioRowMapper())
                );

        this.insUsuarioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_USUARIO")
                .declareParameters(
                        new SqlParameter("p_usuario", Types.VARCHAR),
                        new SqlParameter("p_contrasena", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR),
                        new SqlParameter("p_rol", Types.INTEGER)
                );

        this.updUsuarioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_USUARIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_usuario", Types.VARCHAR),
                        new SqlParameter("p_contrasena", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR),
                        new SqlParameter("p_rol", Types.INTEGER)
                );

        this.delUsuarioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_USUARIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getUsuarioPorNombreCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_USUARIO_POR_NOMBRE")
                .declareParameters(
                        new SqlParameter("p_usuario", Types.VARCHAR),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, new UsuarioRowMapper())
                );
    }

    private static class UsuarioRowMapper implements RowMapper<Usuario> {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario u = new Usuario();
            u.setPkUsuario(rs.getInt("PK_USUARIO"));
            u.setUsuario(rs.getString("USUARIO"));
            u.setContrasena(rs.getString("CONTRASENA"));
            u.setEstado(rs.getString("ESTADO"));
            int rolId = rs.getInt("FK_ROL");
            if (rolId > 0) {
                Rol rol = new Rol();
                rol.setPkRol(rolId);
                u.setRol(rol);
            }
            return u;
        }
    }

    public Optional<Usuario> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getUsuarioCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Usuario> list = (List<Usuario>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Usuario> findAll() {
        String sql = "SELECT * FROM USUARIO";
        return jdbcTemplate.query(sql, new UsuarioRowMapper());
    }

    public Usuario save(Usuario usuario) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_usuario", usuario.getUsuario())
                .addValue("p_contrasena", usuario.getContrasena())
                .addValue("p_estado", usuario.getEstado())
                .addValue("p_rol", usuario.getRol() != null ? usuario.getRol().getPkRol() : 1);
        insUsuarioCall.execute(params);
        // Recuperar ID por nombre de usuario
        String sql = "SELECT PK_USUARIO FROM USUARIO WHERE USUARIO = ?";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, usuario.getUsuario());
        if (!ids.isEmpty()) {
            usuario.setPkUsuario(ids.get(0));
        }
        return usuario;
    }

    public void update(Usuario usuario) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id", usuario.getPkUsuario())
                .addValue("p_usuario", usuario.getUsuario())
                .addValue("p_contrasena", usuario.getContrasena())
                .addValue("p_estado", usuario.getEstado())
                .addValue("p_rol", usuario.getRol() != null ? usuario.getRol().getPkRol() : 1);
        updUsuarioCall.execute(params);
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delUsuarioCall.execute(params);
    }

    public Optional<Usuario> findByUsuario(String username) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_usuario", username);
        Map<String, Object> result = getUsuarioPorNombreCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Usuario> list = (List<Usuario>) result.get("p_cursor");
        return list.stream().findFirst();
    }
}