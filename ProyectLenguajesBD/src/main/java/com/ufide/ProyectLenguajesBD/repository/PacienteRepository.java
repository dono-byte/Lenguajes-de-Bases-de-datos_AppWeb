package com.ufide.ProyectLenguajesBD.repository;

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
public class PacienteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getPacienteCall;
    private final SimpleJdbcCall insPacienteCall;
    private final SimpleJdbcCall updPacienteCall;
    private final SimpleJdbcCall delPacienteCall;
    private final PacienteRowMapper rowMapper;

    public PacienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new PacienteRowMapper();

        this.getPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_cedula", Types.VARCHAR),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_fecha_nacimiento", Types.DATE),
                        new SqlParameter("p_genero", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_direccion", Types.VARCHAR)
                );

        this.updPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_cedula", Types.VARCHAR),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_fecha_nacimiento", Types.DATE),
                        new SqlParameter("p_genero", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_direccion", Types.VARCHAR)
                );

        this.delPacienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_PACIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class PacienteRowMapper implements RowMapper<Paciente> {
        @Override
        public Paciente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Paciente p = new Paciente();
            p.setPkPaciente(rs.getInt("PK_PACIENTE"));
            p.setCedula(rs.getString("CEDULA"));
            p.setNombre(rs.getString("NOMBRE"));
            p.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO").toLocalDate());
            p.setGenero(rs.getString("GENERO"));
            p.setTelefono(rs.getString("TELEFONO"));
            p.setDireccion(rs.getString("DIRECCION"));
            return p;
        }
    }

    public Optional<Paciente> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getPacienteCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Paciente> list = (List<Paciente>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<Paciente> findAll() {
        String sql = "SELECT * FROM PACIENTE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Paciente save(Paciente paciente) {
        if (paciente.getPkPaciente() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_cedula", paciente.getCedula())
                    .addValue("p_nombre", paciente.getNombre())
                    .addValue("p_fecha_nacimiento", Date.valueOf(paciente.getFechaNacimiento()))
                    .addValue("p_genero", paciente.getGenero())
                    .addValue("p_telefono", paciente.getTelefono())
                    .addValue("p_direccion", paciente.getDireccion());
            insPacienteCall.execute(params);
            // Recuperar por cédula
            String sql = "SELECT PK_PACIENTE FROM PACIENTE WHERE CEDULA = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, paciente.getCedula());
            if (!ids.isEmpty()) {
                paciente.setPkPaciente(ids.get(0));
            }
            return paciente;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", paciente.getPkPaciente())
                    .addValue("p_cedula", paciente.getCedula())
                    .addValue("p_nombre", paciente.getNombre())
                    .addValue("p_fecha_nacimiento", Date.valueOf(paciente.getFechaNacimiento()))
                    .addValue("p_genero", paciente.getGenero())
                    .addValue("p_telefono", paciente.getTelefono())
                    .addValue("p_direccion", paciente.getDireccion());
            updPacienteCall.execute(params);
            return paciente;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delPacienteCall.execute(params);
    }

    public Optional<Paciente> findByCedula(String cedula) {
        String sql = "SELECT * FROM PACIENTE WHERE CEDULA = ?";
        List<Paciente> list = jdbcTemplate.query(sql, new Object[]{cedula}, rowMapper);
        return list.stream().findFirst();
    }
}