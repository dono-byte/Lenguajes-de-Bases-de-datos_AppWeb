package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Rol;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class RolRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RolRowMapper rowMapper;

    public RolRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new RolRowMapper();
    }

    private static class RolRowMapper implements RowMapper<Rol> {
        @Override
        public Rol mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rol r = new Rol();
            r.setPkRol(rs.getInt("PK_ROL"));
            r.setNombreRol(rs.getString("NOMBRE_ROL"));
            r.setDescripcion(rs.getString("DESCRIPCION"));
            return r;
        }
    }

    public Optional<Rol> findById(Integer id) {
        String sql = "SELECT * FROM ROL WHERE PK_ROL = ?";
        List<Rol> list = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        return list.stream().findFirst();
    }

    public List<Rol> findAll() {
        String sql = "SELECT * FROM ROL";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Rol save(Rol rol) {
        if (rol.getPkRol() == null) {
            String sql = "INSERT INTO ROL (NOMBRE_ROL, DESCRIPCION) VALUES (?, ?)";
            jdbcTemplate.update(sql, rol.getNombreRol(), rol.getDescripcion());
            String query = "SELECT PK_ROL FROM ROL WHERE NOMBRE_ROL = ?";
            List<Integer> ids = jdbcTemplate.queryForList(query, Integer.class, rol.getNombreRol());
            if (!ids.isEmpty()) {
                rol.setPkRol(ids.get(0));
            }
            return rol;
        } else {
            String sql = "UPDATE ROL SET NOMBRE_ROL = ?, DESCRIPCION = ? WHERE PK_ROL = ?";
            jdbcTemplate.update(sql, rol.getNombreRol(), rol.getDescripcion(), rol.getPkRol());
            return rol;
        }
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM ROL WHERE PK_ROL = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Rol> findByNombreRol(String nombreRol) {
        String sql = "SELECT * FROM ROL WHERE NOMBRE_ROL = ?";
        List<Rol> list = jdbcTemplate.query(sql, new Object[]{nombreRol}, rowMapper);
        return list.stream().findFirst();
    }
}