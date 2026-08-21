package com.ufide.ProyectLenguajesBD.repository;

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
public class MedicamentoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getMedicamentoCall;
    private final SimpleJdbcCall insMedicamentoCall;
    private final SimpleJdbcCall updMedicamentoCall;
    private final SimpleJdbcCall delMedicamentoCall;
    private final MedicamentoRowMapper rowMapper;

    public MedicamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new MedicamentoRowMapper();

        this.getMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_nombre", Types.VARCHAR)
                );

        this.updMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_nombre", Types.VARCHAR)
                );

        this.delMedicamentoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class MedicamentoRowMapper implements RowMapper<Medicamento> {
        @Override
        public Medicamento mapRow(ResultSet rs, int rowNum) throws SQLException {
            Medicamento m = new Medicamento();
            m.setPkMedicamento(rs.getInt("PK_MEDICAMENTO"));
            m.setNombre(rs.getString("NOMBRE"));
            return m;
        }
    }

    public Optional<Medicamento> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getMedicamentoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Medicamento> list = (List<Medicamento>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Medicamento> findAll() {
        String sql = "SELECT * FROM MEDICAMENTOS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Medicamento save(Medicamento medicamento) {
        if (medicamento.getPkMedicamento() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_nombre", medicamento.getNombre());
            insMedicamentoCall.execute(params);
            // Recuperar por nombre
            String sql = "SELECT PK_MEDICAMENTO FROM MEDICAMENTOS WHERE NOMBRE = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, medicamento.getNombre());
            if (!ids.isEmpty()) {
                medicamento.setPkMedicamento(ids.get(0));
            }
            return medicamento;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", medicamento.getPkMedicamento())
                    .addValue("p_nombre", medicamento.getNombre());
            updMedicamentoCall.execute(params);
            return medicamento;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delMedicamentoCall.execute(params);
    }

    public Optional<Medicamento> findByNombre(String nombre) {
        String sql = "SELECT * FROM MEDICAMENTOS WHERE NOMBRE = ?";
        List<Medicamento> list = jdbcTemplate.query(sql, new Object[]{nombre}, rowMapper);
        return list.stream().findFirst();
    }
}