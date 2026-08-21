package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Consultorio;
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
public class ConsultorioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getConsultorioCall;
    private final SimpleJdbcCall insConsultorioCall;
    private final SimpleJdbcCall updConsultorioCall;
    private final SimpleJdbcCall delConsultorioCall;
    private final ConsultorioRowMapper rowMapper;

    public ConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new ConsultorioRowMapper();

        this.getConsultorioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_CONSULTORIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insConsultorioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_CONSULTORIO")
                .declareParameters(
                        new SqlParameter("p_numero_consultorio", Types.VARCHAR),
                        new SqlParameter("p_localidad", Types.VARCHAR),
                        new SqlParameter("p_provincia", Types.VARCHAR)
                );

        this.updConsultorioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_CONSULTORIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_numero_consultorio", Types.VARCHAR),
                        new SqlParameter("p_localidad", Types.VARCHAR),
                        new SqlParameter("p_provincia", Types.VARCHAR)
                );

        this.delConsultorioCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_CONSULTORIO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class ConsultorioRowMapper implements RowMapper<Consultorio> {
        @Override
        public Consultorio mapRow(ResultSet rs, int rowNum) throws SQLException {
            Consultorio c = new Consultorio();
            c.setPkConsultorio(rs.getInt("PK_CONSULTORIO"));
            c.setNumeroConsultorio(rs.getString("NUMERO_CONSULTORIO"));
            c.setLocalidad(rs.getString("LOCALIDAD"));
            c.setProvincia(rs.getString("PROVINCIA"));
            return c;
        }
    }

    public Optional<Consultorio> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getConsultorioCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Consultorio> list = (List<Consultorio>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Consultorio> findAll() {
        String sql = "SELECT * FROM CONSULTORIO";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Consultorio save(Consultorio consultorio) {
        if (consultorio.getPkConsultorio() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_numero_consultorio", consultorio.getNumeroConsultorio())
                    .addValue("p_localidad", consultorio.getLocalidad())
                    .addValue("p_provincia", consultorio.getProvincia());
            insConsultorioCall.execute(params);
            // Recuperar por número
            String sql = "SELECT PK_CONSULTORIO FROM CONSULTORIO WHERE NUMERO_CONSULTORIO = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, consultorio.getNumeroConsultorio());
            if (!ids.isEmpty()) {
                consultorio.setPkConsultorio(ids.get(0));
            }
            return consultorio;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", consultorio.getPkConsultorio())
                    .addValue("p_numero_consultorio", consultorio.getNumeroConsultorio())
                    .addValue("p_localidad", consultorio.getLocalidad())
                    .addValue("p_provincia", consultorio.getProvincia());
            updConsultorioCall.execute(params);
            return consultorio;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delConsultorioCall.execute(params);
    }
}