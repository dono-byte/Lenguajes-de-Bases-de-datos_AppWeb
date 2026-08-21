package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
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
public class PersonalMedicoRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getPersonalMedicoCall;
    private final SimpleJdbcCall insPersonalMedicoCall;
    private final SimpleJdbcCall updPersonalMedicoCall;
    private final SimpleJdbcCall delPersonalMedicoCall;
    private final PersonalMedicoRowMapper rowMapper;

    public PersonalMedicoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new PersonalMedicoRowMapper();

        this.getPersonalMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_PERSONAL_MEDICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insPersonalMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_PERSONAL_MEDICO")
                .declareParameters(
                        new SqlParameter("p_fk_usuario", Types.INTEGER),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_apellido", Types.VARCHAR),
                        new SqlParameter("p_seg_apellido", Types.VARCHAR),
                        new SqlParameter("p_codigo_medico", Types.VARCHAR),
                        new SqlParameter("p_correo_electronico", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR)
                );

        this.updPersonalMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_PERSONAL_MEDICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_usuario", Types.INTEGER),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_apellido", Types.VARCHAR),
                        new SqlParameter("p_seg_apellido", Types.VARCHAR),
                        new SqlParameter("p_codigo_medico", Types.VARCHAR),
                        new SqlParameter("p_correo_electronico", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_estado", Types.VARCHAR)
                );

        this.delPersonalMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_PERSONAL_MEDICO")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class PersonalMedicoRowMapper implements RowMapper<PersonalMedico> {
        @Override
        public PersonalMedico mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersonalMedico pm = new PersonalMedico();
            pm.setPkPersonalMedico(rs.getInt("PK_PERSONAL_MEDICO"));
            pm.setNombre(rs.getString("NOMBRE"));
            pm.setApellido(rs.getString("APELLIDO"));
            pm.setSegApellido(rs.getString("SEG_APELLIDO"));
            pm.setCodigoMedico(rs.getString("CODIGO_MEDICO"));
            pm.setCorreoElectronico(rs.getString("CORREO_ELECTRONICO"));
            pm.setTelefono(rs.getString("TELEFONO"));
            pm.setEstado(rs.getString("ESTADO"));
            int usuarioId = rs.getInt("FK_USUARIO");
            if (usuarioId > 0) {
                Usuario u = new Usuario();
                u.setPkUsuario(usuarioId);
                pm.setUsuario(u);
            }
            return pm;
        }
    }

    public Optional<PersonalMedico> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getPersonalMedicoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<PersonalMedico> list = (List<PersonalMedico>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<PersonalMedico> findAll() {
        String sql = "SELECT * FROM PERSONAL_MEDICO";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public PersonalMedico save(PersonalMedico medico) {
        if (medico.getPkPersonalMedico() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_usuario", medico.getUsuario() != null ? medico.getUsuario().getPkUsuario() : null)
                    .addValue("p_nombre", medico.getNombre())
                    .addValue("p_apellido", medico.getApellido())
                    .addValue("p_seg_apellido", medico.getSegApellido())
                    .addValue("p_codigo_medico", medico.getCodigoMedico())
                    .addValue("p_correo_electronico", medico.getCorreoElectronico())
                    .addValue("p_telefono", medico.getTelefono())
                    .addValue("p_estado", medico.getEstado());
            insPersonalMedicoCall.execute(params);
            // Recuperar por código médico
            String sql = "SELECT PK_PERSONAL_MEDICO FROM PERSONAL_MEDICO WHERE CODIGO_MEDICO = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class, medico.getCodigoMedico());
            if (!ids.isEmpty()) {
                medico.setPkPersonalMedico(ids.get(0));
            }
            return medico;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", medico.getPkPersonalMedico())
                    .addValue("p_fk_usuario", medico.getUsuario() != null ? medico.getUsuario().getPkUsuario() : null)
                    .addValue("p_nombre", medico.getNombre())
                    .addValue("p_apellido", medico.getApellido())
                    .addValue("p_seg_apellido", medico.getSegApellido())
                    .addValue("p_codigo_medico", medico.getCodigoMedico())
                    .addValue("p_correo_electronico", medico.getCorreoElectronico())
                    .addValue("p_telefono", medico.getTelefono())
                    .addValue("p_estado", medico.getEstado());
            updPersonalMedicoCall.execute(params);
            return medico;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delPersonalMedicoCall.execute(params);
    }

    public Optional<PersonalMedico> findByCodigoMedico(String codigo) {
        String sql = "SELECT * FROM PERSONAL_MEDICO WHERE CODIGO_MEDICO = ?";
        List<PersonalMedico> list = jdbcTemplate.query(sql, new Object[]{codigo}, rowMapper);
        return list.stream().findFirst();
    }

    public Optional<PersonalMedico> findByCorreoElectronico(String email) {
        String sql = "SELECT * FROM PERSONAL_MEDICO WHERE CORREO_ELECTRONICO = ?";
        List<PersonalMedico> list = jdbcTemplate.query(sql, new Object[]{email}, rowMapper);
        return list.stream().findFirst();
    }
}