package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Diagnostico;
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
public class DiagnosticoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getDiagnosticoCall;
    private final SimpleJdbcCall insDiagnosticoCall;
    private final SimpleJdbcCall updDiagnosticoCall;
    private final SimpleJdbcCall delDiagnosticoCall;
    private final DiagnosticoRowMapper rowMapper;

    public DiagnosticoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DiagnosticoRowMapper();

        this.getDiagnosticoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_DIAGNOSTICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insDiagnosticoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_DIAGNOSTICO")
                .declareParameters(
                        new SqlParameter("p_codigo_cie10", Types.VARCHAR),
                        new SqlParameter("p_descripcion", Types.VARCHAR)
                );

        this.updDiagnosticoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_DIAGNOSTICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_codigo_cie10", Types.VARCHAR),
                        new SqlParameter("p_descripcion", Types.VARCHAR)
                );

        this.delDiagnosticoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_DIAGNOSTICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class DiagnosticoRowMapper implements RowMapper<Diagnostico> {
        @Override
        public Diagnostico mapRow(ResultSet rs, int rowNum) throws SQLException {
            Diagnostico d = new Diagnostico();
            d.setPkDiagnostico(rs.getInt("PK_DIAGNOSTICO"));
            d.setCodigoCie10(rs.getString("CODIGO_CIE10"));
            d.setDescripcion(rs.getString("DESCRIPCION"));
            return d;
        }
    }

    public Optional<Diagnostico> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getDiagnosticoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Diagnostico> list = (List<Diagnostico>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Diagnostico> findAll() {
        String sql = "SELECT * FROM DIAGNOSTICO";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Diagnostico save(Diagnostico diagnostico) {
        if (diagnostico.getPkDiagnostico() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_codigo_cie10", diagnostico.getCodigoCie10())
                    .addValue("p_descripcion", diagnostico.getDescripcion());
            insDiagnosticoCall.execute(params);
            // Recuperar por código
            String sql = "SELECT PK_DIAGNOSTICO FROM DIAGNOSTICO WHERE CODIGO_CIE10 = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, diagnostico.getCodigoCie10());
            if (!ids.isEmpty()) {
                diagnostico.setPkDiagnostico(ids.get(0));
            }
            return diagnostico;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", diagnostico.getPkDiagnostico())
                    .addValue("p_codigo_cie10", diagnostico.getCodigoCie10())
                    .addValue("p_descripcion", diagnostico.getDescripcion());
            updDiagnosticoCall.execute(params);
            return diagnostico;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delDiagnosticoCall.execute(params);
    }

    public Optional<Diagnostico> findByCodigoCie10(String codigo) {
        String sql = "SELECT * FROM DIAGNOSTICO WHERE CODIGO_CIE10 = ?";
        List<Diagnostico> list = jdbcTemplate.query(sql, new Object[]{codigo}, rowMapper);
        return list.stream().findFirst();
    }
}