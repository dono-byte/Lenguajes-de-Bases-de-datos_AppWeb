package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Receta;
import com.ufide.ProyectLenguajesBD.entity.Consulta;
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
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecetaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getRecetaCall;
    private final SimpleJdbcCall insRecetaCall;
    private final SimpleJdbcCall updRecetaCall;
    private final SimpleJdbcCall delRecetaCall;
    private final SimpleJdbcCall getRecetasPorConsultaCall;
    private final RecetaRowMapper rowMapper;

    public RecetaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new RecetaRowMapper();

        this.getRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_RECETAS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_RECETAS")
                .declareParameters(
                        new SqlParameter("p_fk_consulta", Types.INTEGER),
                        new SqlParameter("p_fecha_emision", Types.DATE)
                );

        this.updRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_RECETAS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_consulta", Types.INTEGER),
                        new SqlParameter("p_fecha_emision", Types.DATE)
                );

        this.delRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_RECETAS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getRecetasPorConsultaCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_RECETAS_POR_CONSULTA")
                .declareParameters(
                        new SqlParameter("p_consulta_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );
    }

    private static class RecetaRowMapper implements RowMapper<Receta> {
        @Override
        public Receta mapRow(ResultSet rs, int rowNum) throws SQLException {
            Receta r = new Receta();
            r.setPkReceta(rs.getInt("PK_RECETA"));
            r.setFechaEmision(rs.getDate("FECHA_EMISION").toLocalDate());
            int consultaId = rs.getInt("FK_CONSULTA");
            if (consultaId > 0) {
                Consulta c = new Consulta();
                c.setPkConsulta(consultaId);
                r.setConsulta(c);
            }
            return r;
        }
    }

    public Optional<Receta> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getRecetaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Receta> list = (List<Receta>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Receta> findAll() {
        String sql = "SELECT * FROM RECETAS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Receta save(Receta receta) {
        if (receta.getPkReceta() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_consulta", receta.getConsulta() != null ? receta.getConsulta().getPkConsulta() : null)
                    .addValue("p_fecha_emision", Date.valueOf(receta.getFechaEmision()));
            insRecetaCall.execute(params);
            // Recuperar por consulta y fecha
            String sql = "SELECT PK_RECETA FROM RECETAS WHERE FK_CONSULTA = ? AND FECHA_EMISION = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    receta.getConsulta().getPkConsulta(),
                    Date.valueOf(receta.getFechaEmision()));
            if (!ids.isEmpty()) {
                receta.setPkReceta(ids.get(0));
            }
            return receta;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", receta.getPkReceta())
                    .addValue("p_fk_consulta", receta.getConsulta() != null ? receta.getConsulta().getPkConsulta() : null)
                    .addValue("p_fecha_emision", Date.valueOf(receta.getFechaEmision()));
            updRecetaCall.execute(params);
            return receta;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delRecetaCall.execute(params);
    }

    public List<Receta> findByConsultaPkConsulta(Integer consultaId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_consulta_id", consultaId);
        Map<String, Object> result = getRecetasPorConsultaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Receta> list = (List<Receta>) result.get("p_cursor");
        return list;
    }
}