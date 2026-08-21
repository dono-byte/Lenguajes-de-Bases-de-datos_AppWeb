package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
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
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import oracle.jdbc.OracleTypes;
@Repository
public class CitaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getCitaCall;
    private final SimpleJdbcCall insCitaCall;
    private final SimpleJdbcCall updCitaCall;
    private final SimpleJdbcCall delCitaCall;
    private final SimpleJdbcCall getCitasPorPacienteCall;
    private final SimpleJdbcCall getCitasPorRangoCall;
    private final CitaRowMapper rowMapper;

    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new CitaRowMapper();

        this.getCitaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_CITA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insCitaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_CITA")
                .declareParameters(
                        new SqlParameter("p_fk_paciente", Types.INTEGER),
                        new SqlParameter("p_fk_consultorio", Types.INTEGER),
                        new SqlParameter("p_fecha_hora", Types.TIMESTAMP),
                        new SqlParameter("p_duracion", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR)
                );

        this.updCitaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_CITA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_paciente", Types.INTEGER),
                        new SqlParameter("p_fk_consultorio", Types.INTEGER),
                        new SqlParameter("p_fecha_hora", Types.TIMESTAMP),
                        new SqlParameter("p_duracion", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR)
                );

        this.delCitaCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_CITA")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );

        this.getCitasPorPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_CITAS_POR_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_paciente_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );

        this.getCitasPorRangoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_CITAS_POR_RANGO")
                .declareParameters(
                        new SqlParameter("p_fecha_inicio", Types.DATE),
                        new SqlParameter("p_fecha_fin", Types.DATE),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR, rowMapper)
                );
    }

    private static class CitaRowMapper implements RowMapper<Cita> {
        @Override
        public Cita mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cita c = new Cita();
            c.setPkCita(rs.getInt("PK_CITA"));
            c.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
            c.setDuracion(rs.getString("DURACION"));
            c.setEstado(rs.getString("ESTADO"));

            int pacienteId = rs.getInt("FK_PACIENTE");
            if (pacienteId > 0) {
                Paciente p = new Paciente();
                p.setPkPaciente(pacienteId);
                c.setPaciente(p);
            }
            int consultorioId = rs.getInt("FK_CONSULTORIO");
            if (consultorioId > 0) {
                Consultorio con = new Consultorio();
                con.setPkConsultorio(consultorioId);
                c.setConsultorio(con);
            }
            return c;
        }
    }

    public Optional<Cita> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getCitaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Cita> list = (List<Cita>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Cita> findAll() {
        String sql = "SELECT * FROM CITA";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Cita save(Cita cita) {
        if (cita.getPkCita() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_paciente", cita.getPaciente() != null ? cita.getPaciente().getPkPaciente() : null)
                    .addValue("p_fk_consultorio", cita.getConsultorio() != null ? cita.getConsultorio().getPkConsultorio() : null)
                    .addValue("p_fecha_hora", cita.getFechaHora())
                    .addValue("p_duracion", cita.getDuracion())
                    .addValue("p_estado", cita.getEstado());
            insCitaCall.execute(params);
            // Recuperar ID por paciente y fecha
            String sql = "SELECT PK_CITA FROM CITA WHERE FK_PACIENTE = ? AND FECHA_HORA = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    cita.getPaciente().getPkPaciente(),
                    Timestamp.valueOf(cita.getFechaHora()));
            if (!ids.isEmpty()) {
                cita.setPkCita(ids.get(0));
            }
            return cita;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", cita.getPkCita())
                    .addValue("p_fk_paciente", cita.getPaciente() != null ? cita.getPaciente().getPkPaciente() : null)
                    .addValue("p_fk_consultorio", cita.getConsultorio() != null ? cita.getConsultorio().getPkConsultorio() : null)
                    .addValue("p_fecha_hora", cita.getFechaHora())
                    .addValue("p_duracion", cita.getDuracion())
                    .addValue("p_estado", cita.getEstado());
            updCitaCall.execute(params);
            return cita;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delCitaCall.execute(params);
    }

    public List<Cita> findByPacientePkPaciente(Integer pacienteId) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_paciente_id", pacienteId);
        Map<String, Object> result = getCitasPorPacienteCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Cita> list = (List<Cita>) result.get("p_cursor");
        return list;
    }

    public List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_fecha_inicio", Timestamp.valueOf(inicio))
                .addValue("p_fecha_fin", Timestamp.valueOf(fin));
        Map<String, Object> result = getCitasPorRangoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Cita> list = (List<Cita>) result.get("p_cursor");
        return list;
    }
}