package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.entity.Diagnostico;
import com.ufide.ProyectLenguajesBD.entity.Cita;
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
public class ConsultaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getConsultaCall;
    private final SimpleJdbcCall insConsultaCall;
    private final SimpleJdbcCall updConsultaCall;
    private final SimpleJdbcCall delConsultaCall;
    private final SimpleJdbcCall getConsultasPorExpedienteCall;
    private final SimpleJdbcCall getConsultasPorMedicoCall;
    private final ConsultaRowMapper rowMapper;

    public ConsultaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new ConsultaRowMapper();

        this.getConsultaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_CONSULTA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insConsultaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_CONSULTA")
                .declareParameters(
                        new SqlParameter("p_fk_expediente", Types.INTEGER),
                        new SqlParameter("p_fk_personal_medico", Types.INTEGER),
                        new SqlParameter("p_fk_diagnostico", Types.INTEGER),
                        new SqlParameter("p_fk_cita", Types.INTEGER),
                        new SqlParameter("p_fecha_consulta", Types.DATE),
                        new SqlParameter("p_motivo", Types.VARCHAR),
                        new SqlParameter("p_observaciones", Types.VARCHAR)
                );

        this.updConsultaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_CONSULTA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_expediente", Types.INTEGER),
                        new SqlParameter("p_fk_personal_medico", Types.INTEGER),
                        new SqlParameter("p_fk_diagnostico", Types.INTEGER),
                        new SqlParameter("p_fk_cita", Types.INTEGER),
                        new SqlParameter("p_fecha_consulta", Types.DATE),
                        new SqlParameter("p_motivo", Types.VARCHAR),
                        new SqlParameter("p_observaciones", Types.VARCHAR)
                );

        this.delConsultaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_CONSULTA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getConsultasPorExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_CONSULTAS_POR_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_expediente_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );

        this.getConsultasPorMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_CONSULTAS_POR_MEDICO")
                .declareParameters(
                        new SqlParameter("p_medico_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );
    }

    private static class ConsultaRowMapper implements RowMapper<Consulta> {
        @Override
        public Consulta mapRow(ResultSet rs, int rowNum) throws SQLException {
            Consulta c = new Consulta();
            c.setPkConsulta(rs.getInt("PK_CONSULTA"));
            c.setFechaConsulta(rs.getDate("FECHA_CONSULTA").toLocalDate());
            c.setMotivo(rs.getString("MOTIVO"));
            c.setObservaciones(rs.getString("OBSERVACIONES"));

            int expedienteId = rs.getInt("FK_EXPEDIENTE");
            if (expedienteId > 0) {
                Expediente e = new Expediente();
                e.setPkExpediente(expedienteId);
                c.setExpediente(e);
            }
            int medicoId = rs.getInt("FK_PERSONAL_MEDICO");
            if (medicoId > 0) {
                PersonalMedico pm = new PersonalMedico();
                pm.setPkPersonalMedico(medicoId);
                c.setPersonalMedico(pm);
            }
            int diagnosticoId = rs.getInt("FK_DIAGNOSTICO");
            if (diagnosticoId > 0) {
                Diagnostico d = new Diagnostico();
                d.setPkDiagnostico(diagnosticoId);
                c.setDiagnostico(d);
            }
            int citaId = rs.getInt("FK_CITA");
            if (citaId > 0) {
                Cita cit = new Cita();
                cit.setPkCita(citaId);
                c.setCita(cit);
            }
            return c;
        }
    }

    public Optional<Consulta> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getConsultaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Consulta> list = (List<Consulta>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Consulta> findAll() {
        String sql = "SELECT * FROM CONSULTA";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Consulta save(Consulta consulta) {
        if (consulta.getPkConsulta() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_expediente", consulta.getExpediente() != null ? consulta.getExpediente().getPkExpediente() : null)
                    .addValue("p_fk_personal_medico", consulta.getPersonalMedico() != null ? consulta.getPersonalMedico().getPkPersonalMedico() : null)
                    .addValue("p_fk_diagnostico", consulta.getDiagnostico() != null ? consulta.getDiagnostico().getPkDiagnostico() : null)
                    .addValue("p_fk_cita", consulta.getCita() != null ? consulta.getCita().getPkCita() : null)
                    .addValue("p_fecha_consulta", Date.valueOf(consulta.getFechaConsulta()))
                    .addValue("p_motivo", consulta.getMotivo())
                    .addValue("p_observaciones", consulta.getObservaciones());
            insConsultaCall.execute(params);
            // Recuperar ID por cita y fecha
            String sql = "SELECT PK_CONSULTA FROM CONSULTA WHERE FK_CITA = ? AND FECHA_CONSULTA = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    consulta.getCita().getPkCita(),
                    Date.valueOf(consulta.getFechaConsulta()));
            if (!ids.isEmpty()) {
                consulta.setPkConsulta(ids.get(0));
            }
            return consulta;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", consulta.getPkConsulta())
                    .addValue("p_fk_expediente", consulta.getExpediente() != null ? consulta.getExpediente().getPkExpediente() : null)
                    .addValue("p_fk_personal_medico", consulta.getPersonalMedico() != null ? consulta.getPersonalMedico().getPkPersonalMedico() : null)
                    .addValue("p_fk_diagnostico", consulta.getDiagnostico() != null ? consulta.getDiagnostico().getPkDiagnostico() : null)
                    .addValue("p_fk_cita", consulta.getCita() != null ? consulta.getCita().getPkCita() : null)
                    .addValue("p_fecha_consulta", Date.valueOf(consulta.getFechaConsulta()))
                    .addValue("p_motivo", consulta.getMotivo())
                    .addValue("p_observaciones", consulta.getObservaciones());
            updConsultaCall.execute(params);
            return consulta;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delConsultaCall.execute(params);
    }

    public List<Consulta> findByExpedientePkExpediente(Integer expedienteId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_expediente_id", expedienteId);
        Map<String, Object> result = getConsultasPorExpedienteCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Consulta> list = (List<Consulta>) result.get("p_cursor");
        return list;
    }

    public List<Consulta> findByPersonalMedicoPkPersonalMedico(Integer medicoId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_medico_id", medicoId);
        Map<String, Object> result = getConsultasPorMedicoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Consulta> list = (List<Consulta>) result.get("p_cursor");
        return list;
    }
}