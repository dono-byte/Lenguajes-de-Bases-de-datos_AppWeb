package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.entity.Receta;
import com.ufide.ProyectLenguajesBD.entity.Medicamento;
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
public class DetalleRecetaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getDetalleRecetaCall;
    private final SimpleJdbcCall insDetalleRecetaCall;
    private final SimpleJdbcCall updDetalleRecetaCall;
    private final SimpleJdbcCall delDetalleRecetaCall;
    private final DetalleRecetaRowMapper rowMapper;

    public DetalleRecetaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DetalleRecetaRowMapper();

        this.getDetalleRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_DETALLE_RECETA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insDetalleRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_DETALLE_RECETA")
                .declareParameters(
                        new SqlParameter("p_fk_receta", Types.INTEGER),
                        new SqlParameter("p_fk_medicamentos", Types.INTEGER),
                        new SqlParameter("p_dosis", Types.VARCHAR),
                        new SqlParameter("p_frecuencia", Types.VARCHAR)
                );

        this.updDetalleRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_DETALLE_RECETA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_receta", Types.INTEGER),
                        new SqlParameter("p_fk_medicamentos", Types.INTEGER),
                        new SqlParameter("p_dosis", Types.VARCHAR),
                        new SqlParameter("p_frecuencia", Types.VARCHAR)
                );

        this.delDetalleRecetaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_DETALLE_RECETA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class DetalleRecetaRowMapper implements RowMapper<DetalleReceta> {
        @Override
        public DetalleReceta mapRow(ResultSet rs, int rowNum) throws SQLException {
            DetalleReceta dr = new DetalleReceta();
            dr.setPkDetalleReceta(rs.getInt("PK_DETALLE_RECETA"));
            dr.setDosis(rs.getString("DOSIS"));
            dr.setFrecuencia(rs.getString("FRECUENCIA"));

            int recetaId = rs.getInt("FK_RECETA");
            if (recetaId > 0) {
                Receta r = new Receta();
                r.setPkReceta(recetaId);
                dr.setReceta(r);
            }
            int medId = rs.getInt("FK_MEDICAMENTOS");
            if (medId > 0) {
                Medicamento m = new Medicamento();
                m.setPkMedicamento(medId);
                dr.setMedicamento(m);
            }
            return dr;
        }
    }

    public Optional<DetalleReceta> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getDetalleRecetaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<DetalleReceta> list = (List<DetalleReceta>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<DetalleReceta> findAll() {
        String sql = "SELECT * FROM DETALLE_RECETA";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public DetalleReceta save(DetalleReceta detalle) {
        if (detalle.getPkDetalleReceta() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_receta", detalle.getReceta() != null ? detalle.getReceta().getPkReceta() : null)
                    .addValue("p_fk_medicamentos", detalle.getMedicamento() != null ? detalle.getMedicamento().getPkMedicamento() : null)
                    .addValue("p_dosis", detalle.getDosis())
                    .addValue("p_frecuencia", detalle.getFrecuencia());
            insDetalleRecetaCall.execute(params);
            // Recuperar ID por receta y medicamento
            String sql = "SELECT PK_DETALLE_RECETA FROM DETALLE_RECETA WHERE FK_RECETA = ? AND FK_MEDICAMENTOS = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    detalle.getReceta().getPkReceta(),
                    detalle.getMedicamento().getPkMedicamento());
            if (!ids.isEmpty()) {
                detalle.setPkDetalleReceta(ids.get(0));
            }
            return detalle;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", detalle.getPkDetalleReceta())
                    .addValue("p_fk_receta", detalle.getReceta() != null ? detalle.getReceta().getPkReceta() : null)
                    .addValue("p_fk_medicamentos", detalle.getMedicamento() != null ? detalle.getMedicamento().getPkMedicamento() : null)
                    .addValue("p_dosis", detalle.getDosis())
                    .addValue("p_frecuencia", detalle.getFrecuencia());
            updDetalleRecetaCall.execute(params);
            return detalle;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delDetalleRecetaCall.execute(params);
    }

    public List<DetalleReceta> findByRecetaPkReceta(Integer recetaId) {
        String sql = "SELECT * FROM DETALLE_RECETA WHERE FK_RECETA = ?";
        return jdbcTemplate.query(sql, new Object[]{recetaId}, rowMapper);
    }
}