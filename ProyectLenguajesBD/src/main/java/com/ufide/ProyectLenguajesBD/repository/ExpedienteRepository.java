package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
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
public class ExpedienteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getExpedienteCall;
    private final SimpleJdbcCall insExpedienteCall;
    private final SimpleJdbcCall updExpedienteCall;
    private final SimpleJdbcCall delExpedienteCall;
    private final SimpleJdbcCall getExpedientePorPacienteCall;
    private final ExpedienteRowMapper rowMapper;

    public ExpedienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new ExpedienteRowMapper();

        this.getExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_fk_paciente", Types.INTEGER),
                        new SqlParameter("p_fecha_creacion", Types.DATE)
                );

        this.updExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_paciente", Types.INTEGER),
                        new SqlParameter("p_fecha_creacion", Types.DATE)
                );

        this.delExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getExpedientePorPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_EXPEDIENTE_POR_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_paciente_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );
    }

    private static class ExpedienteRowMapper implements RowMapper<Expediente> {
        @Override
        public Expediente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expediente e = new Expediente();
            e.setPkExpediente(rs.getInt("PK_EXPEDIENTE"));
            e.setFechaCreacion(rs.getDate("FECHA_CREACION").toLocalDate());
            int pacienteId = rs.getInt("FK_PACIENTE");
            if (pacienteId > 0) {
                Paciente p = new Paciente();
                p.setPkPaciente(pacienteId);
                e.setPaciente(p);
            }
            return e;
        }
    }

    public Optional<Expediente> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getExpedienteCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Expediente> list = (List<Expediente>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Expediente> findAll() {
        String sql = "SELECT * FROM EXPEDIENTE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Expediente save(Expediente expediente) {
        if (expediente.getPkExpediente() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_paciente", expediente.getPaciente() != null ? expediente.getPaciente().getPkPaciente() : null)
                    .addValue("p_fecha_creacion", Date.valueOf(expediente.getFechaCreacion()));
            insExpedienteCall.execute(params);
            // Recuperar por paciente
            String sql = "SELECT PK_EXPEDIENTE FROM EXPEDIENTE WHERE FK_PACIENTE = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    expediente.getPaciente().getPkPaciente());
            if (!ids.isEmpty()) {
                expediente.setPkExpediente(ids.get(0));
            }
            return expediente;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", expediente.getPkExpediente())
                    .addValue("p_fk_paciente", expediente.getPaciente() != null ? expediente.getPaciente().getPkPaciente() : null)
                    .addValue("p_fecha_creacion", Date.valueOf(expediente.getFechaCreacion()));
            updExpedienteCall.execute(params);
            return expediente;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delExpedienteCall.execute(params);
    }

    public Optional<Expediente> findByPacientePkPaciente(Integer pacienteId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_paciente_id", pacienteId);
        Map<String, Object> result = getExpedientePorPacienteCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Expediente> list = (List<Expediente>) result.get("p_cursor");
        return list.stream().findFirst();
    }
}