package com.ufide.ProyectLenguajesBD.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.entity.Consultorio;
import com.ufide.ProyectLenguajesBD.entity.Paciente;

@Repository
public class CitaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall insCitaCall;
    private final SimpleJdbcCall updCitaCall;
    private final SimpleJdbcCall delCitaCall;
    private final CitaRowMapper rowMapper;

    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new CitaRowMapper();

        // Procedimientos para escritura (INSERT, UPDATE, DELETE) se mantienen
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
    }

    // RowMapper que mapea todas las columnas incluyendo las de PACIENTE y CONSULTORIO
    private static class CitaRowMapper implements RowMapper<Cita> {
        @Override
        public Cita mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cita c = new Cita();
            c.setPkCita(rs.getInt("PK_CITA"));
            c.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
            c.setDuracion(rs.getString("DURACION"));
            c.setEstado(rs.getString("ESTADO"));

            // Paciente
            Paciente p = new Paciente();
            p.setPkPaciente(rs.getInt("FK_PACIENTE"));
            p.setNombre(rs.getString("paciente_nombre"));
            p.setCedula(rs.getString("paciente_cedula"));
            p.setFechaNacimiento(rs.getDate("paciente_fecha_nac").toLocalDate());
            p.setGenero(rs.getString("paciente_genero"));
            p.setTelefono(rs.getString("paciente_telefono"));
            p.setDireccion(rs.getString("paciente_direccion"));
            c.setPaciente(p);

            // Consultorio
            Consultorio con = new Consultorio();
            con.setPkConsultorio(rs.getInt("FK_CONSULTORIO"));
            con.setNumeroConsultorio(rs.getString("consultorio_numero"));
            con.setLocalidad(rs.getString("consultorio_localidad"));
            con.setProvincia(rs.getString("consultorio_provincia"));
            c.setConsultorio(con);

            return c;
        }
    }

    // ============ MÉTODOS DE LECTURA (con JOIN) ============

    public Optional<Cita> findById(Integer id) {
        String sql = "SELECT c.*, " +
                     "p.NOMBRE as paciente_nombre, p.CEDULA as paciente_cedula, " +
                     "p.FECHA_NACIMIENTO as paciente_fecha_nac, p.GENERO as paciente_genero, " +
                     "p.TELEFONO as paciente_telefono, p.DIRECCION as paciente_direccion, " +
                     "con.NUMERO_CONSULTORIO as consultorio_numero, " +
                     "con.LOCALIDAD as consultorio_localidad, con.PROVINCIA as consultorio_provincia " +
                     "FROM CITA c " +
                     "JOIN PACIENTE p ON c.FK_PACIENTE = p.PK_PACIENTE " +
                     "JOIN CONSULTORIO con ON c.FK_CONSULTORIO = con.PK_CONSULTORIO " +
                     "WHERE c.PK_CITA = ?";
        List<Cita> result = jdbcTemplate.query(sql, new Object[]{id}, rowMapper);
        return result.stream().findFirst();
    }

    public List<Cita> findAll() {
        String sql = "SELECT c.*, " +
                     "p.NOMBRE as paciente_nombre, p.CEDULA as paciente_cedula, " +
                     "p.FECHA_NACIMIENTO as paciente_fecha_nac, p.GENERO as paciente_genero, " +
                     "p.TELEFONO as paciente_telefono, p.DIRECCION as paciente_direccion, " +
                     "con.NUMERO_CONSULTORIO as consultorio_numero, " +
                     "con.LOCALIDAD as consultorio_localidad, con.PROVINCIA as consultorio_provincia " +
                     "FROM CITA c " +
                     "JOIN PACIENTE p ON c.FK_PACIENTE = p.PK_PACIENTE " +
                     "JOIN CONSULTORIO con ON c.FK_CONSULTORIO = con.PK_CONSULTORIO";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Cita> findByPacientePkPaciente(Integer pacienteId) {
        String sql = "SELECT c.*, " +
                     "p.NOMBRE as paciente_nombre, p.CEDULA as paciente_cedula, " +
                     "p.FECHA_NACIMIENTO as paciente_fecha_nac, p.GENERO as paciente_genero, " +
                     "p.TELEFONO as paciente_telefono, p.DIRECCION as paciente_direccion, " +
                     "con.NUMERO_CONSULTORIO as consultorio_numero, " +
                     "con.LOCALIDAD as consultorio_localidad, con.PROVINCIA as consultorio_provincia " +
                     "FROM CITA c " +
                     "JOIN PACIENTE p ON c.FK_PACIENTE = p.PK_PACIENTE " +
                     "JOIN CONSULTORIO con ON c.FK_CONSULTORIO = con.PK_CONSULTORIO " +
                     "WHERE c.FK_PACIENTE = ?";
        return jdbcTemplate.query(sql, new Object[]{pacienteId}, rowMapper);
    }

    public List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT c.*, " +
                     "p.NOMBRE as paciente_nombre, p.CEDULA as paciente_cedula, " +
                     "p.FECHA_NACIMIENTO as paciente_fecha_nac, p.GENERO as paciente_genero, " +
                     "p.TELEFONO as paciente_telefono, p.DIRECCION as paciente_direccion, " +
                     "con.NUMERO_CONSULTORIO as consultorio_numero, " +
                     "con.LOCALIDAD as consultorio_localidad, con.PROVINCIA as consultorio_provincia " +
                     "FROM CITA c " +
                     "JOIN PACIENTE p ON c.FK_PACIENTE = p.PK_PACIENTE " +
                     "JOIN CONSULTORIO con ON c.FK_CONSULTORIO = con.PK_CONSULTORIO " +
                     "WHERE c.FECHA_HORA BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{Timestamp.valueOf(inicio), Timestamp.valueOf(fin)}, rowMapper);
    }

    // ============ MÉTODOS DE ESCRITURA (procedimientos) ============

    public Cita save(Cita cita) {
        if (cita.getPkCita() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_paciente", cita.getPaciente().getPkPaciente())
                    .addValue("p_fk_consultorio", cita.getConsultorio().getPkConsultorio())
                    .addValue("p_fecha_hora", cita.getFechaHora())
                    .addValue("p_duracion", cita.getDuracion())
                    .addValue("p_estado", cita.getEstado());
            insCitaCall.execute(params);
            // Recuperar el ID generado
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
                    .addValue("p_fk_paciente", cita.getPaciente().getPkPaciente())
                    .addValue("p_fk_consultorio", cita.getConsultorio().getPkConsultorio())
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
}