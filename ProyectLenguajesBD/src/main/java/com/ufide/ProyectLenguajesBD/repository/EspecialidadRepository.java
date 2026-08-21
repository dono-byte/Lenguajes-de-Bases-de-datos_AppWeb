package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Especialidad;
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
public class EspecialidadRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getEspecialidadCall;
    private final SimpleJdbcCall insEspecialidadCall;
    private final SimpleJdbcCall updEspecialidadCall;
    private final SimpleJdbcCall delEspecialidadCall;
    private final EspecialidadRowMapper rowMapper;

    public EspecialidadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new EspecialidadRowMapper();

        this.getEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_descripcion", Types.VARCHAR)
                );

        this.updEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_descripcion", Types.VARCHAR)
                );

        this.delEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class EspecialidadRowMapper implements RowMapper<Especialidad> {
        @Override
        public Especialidad mapRow(ResultSet rs, int rowNum) throws SQLException {
            Especialidad e = new Especialidad();
            e.setPkEspecialidad(rs.getInt("PK_ESPECIALIDAD"));
            e.setNombre(rs.getString("NOMBRE"));
            e.setDescripcion(rs.getString("DESCRIPCION"));
            return e;
        }
    }

    public Optional<Especialidad> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getEspecialidadCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Especialidad> list = (List<Especialidad>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Especialidad> findAll() {
        String sql = "SELECT * FROM ESPECIALIDAD";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Especialidad save(Especialidad especialidad) {
        if (especialidad.getPkEspecialidad() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_nombre", especialidad.getNombre())
                    .addValue("p_descripcion", especialidad.getDescripcion());
            insEspecialidadCall.execute(params);
            // Recuperar por nombre
            String sql = "SELECT PK_ESPECIALIDAD FROM ESPECIALIDAD WHERE NOMBRE = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, especialidad.getNombre());
            if (!ids.isEmpty()) {
                especialidad.setPkEspecialidad(ids.get(0));
            }
            return especialidad;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", especialidad.getPkEspecialidad())
                    .addValue("p_nombre", especialidad.getNombre())
                    .addValue("p_descripcion", especialidad.getDescripcion());
            updEspecialidadCall.execute(params);
            return especialidad;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delEspecialidadCall.execute(params);
    }
}