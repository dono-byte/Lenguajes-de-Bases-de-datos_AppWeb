package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;
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
import java.sql.Date;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DetalleMedicamentoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getDetalleMedicamentoCall;
    private final SimpleJdbcCall insDetalleMedicamentoCall;
    private final SimpleJdbcCall updDetalleMedicamentoCall;
    private final SimpleJdbcCall delDetalleMedicamentoCall;
    private final SimpleJdbcCall getDetallesPorMedicamentoCall;
    private final DetalleMedicamentoRowMapper rowMapper;

    public DetalleMedicamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new DetalleMedicamentoRowMapper();

        this.getDetalleMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_DETALLE_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insDetalleMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_DETALLE_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_fk_medicamento", Types.INTEGER),
                        new SqlParameter("p_presentacion", Types.VARCHAR),
                        new SqlParameter("p_concentracion", Types.VARCHAR),
                        new SqlParameter("p_entradas", Types.INTEGER),
                        new SqlParameter("p_salidas", Types.INTEGER),
                        new SqlParameter("p_lotes", Types.VARCHAR),
                        new SqlParameter("p_vencimientos", Types.DATE)
                );

        this.updDetalleMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_DETALLE_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_medicamento", Types.INTEGER),
                        new SqlParameter("p_presentacion", Types.VARCHAR),
                        new SqlParameter("p_concentracion", Types.VARCHAR),
                        new SqlParameter("p_entradas", Types.INTEGER),
                        new SqlParameter("p_salidas", Types.INTEGER),
                        new SqlParameter("p_lotes", Types.VARCHAR),
                        new SqlParameter("p_vencimientos", Types.DATE)
                );

        this.delDetalleMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_DETALLE_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getDetallesPorMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_DETALLE_MEDICAMENTOS_POR_MEDICAMENTO")
                .declareParameters(
                        new SqlParameter("p_medicamento_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );
    }

    private static class DetalleMedicamentoRowMapper implements RowMapper<DetalleMedicamento> {
        @Override
        public DetalleMedicamento mapRow(ResultSet rs, int rowNum) throws SQLException {
            DetalleMedicamento dm = new DetalleMedicamento();
            dm.setPkDetalleMedicamento(rs.getInt("PK_DETALLE_MEDICAMENTOS"));
            dm.setPresentacion(rs.getString("PRESENTACION"));
            dm.setConcentracion(rs.getString("CONCENTRACION"));
            dm.setEntradas(rs.getInt("ENTRADAS"));
            dm.setSalidas(rs.getInt("SALIDAS"));
            dm.setLotes(rs.getString("LOTES"));
            Date venc = rs.getDate("VENCIMIENTOS");
            if (venc != null) {
                dm.setVencimientos(venc.toLocalDate());
            }
            int medId = rs.getInt("FK_MEDICAMENTO");
            if (medId > 0) {
                Medicamento m = new Medicamento();
                m.setPkMedicamento(medId);
                dm.setMedicamento(m);
            }
            return dm;
        }
    }

    public Optional<DetalleMedicamento> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getDetalleMedicamentoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<DetalleMedicamento> list = (List<DetalleMedicamento>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<DetalleMedicamento> findAll() {
        String sql = "SELECT * FROM DETALLE_MEDICAMENTOS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public DetalleMedicamento save(DetalleMedicamento detalle) {
        if (detalle.getPkDetalleMedicamento() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_medicamento", detalle.getMedicamento() != null ? detalle.getMedicamento().getPkMedicamento() : null)
                    .addValue("p_presentacion", detalle.getPresentacion())
                    .addValue("p_concentracion", detalle.getConcentracion())
                    .addValue("p_entradas", detalle.getEntradas())
                    .addValue("p_salidas", detalle.getSalidas())
                    .addValue("p_lotes", detalle.getLotes())
                    .addValue("p_vencimientos", detalle.getVencimientos() != null ? Date.valueOf(detalle.getVencimientos()) : null);
            insDetalleMedicamentoCall.execute(params);
            // Recuperar por lote y medicamento
            String sql = "SELECT PK_DETALLE_MEDICAMENTOS FROM DETALLE_MEDICAMENTOS WHERE FK_MEDICAMENTO = ? AND LOTES = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    detalle.getMedicamento().getPkMedicamento(),
                    detalle.getLotes());
            if (!ids.isEmpty()) {
                detalle.setPkDetalleMedicamento(ids.get(0));
            }
            return detalle;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", detalle.getPkDetalleMedicamento())
                    .addValue("p_fk_medicamento", detalle.getMedicamento() != null ? detalle.getMedicamento().getPkMedicamento() : null)
                    .addValue("p_presentacion", detalle.getPresentacion())
                    .addValue("p_concentracion", detalle.getConcentracion())
                    .addValue("p_entradas", detalle.getEntradas())
                    .addValue("p_salidas", detalle.getSalidas())
                    .addValue("p_lotes", detalle.getLotes())
                    .addValue("p_vencimientos", detalle.getVencimientos() != null ? Date.valueOf(detalle.getVencimientos()) : null);
            updDetalleMedicamentoCall.execute(params);
            return detalle;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delDetalleMedicamentoCall.execute(params);
    }

    public List<DetalleMedicamento> findByMedicamentoPkMedicamento(Integer medicamentoId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_medicamento_id", medicamentoId);
        Map<String, Object> result = getDetallesPorMedicamentoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<DetalleMedicamento> list = (List<DetalleMedicamento>) result.get("p_cursor");
        return list;
    }
}