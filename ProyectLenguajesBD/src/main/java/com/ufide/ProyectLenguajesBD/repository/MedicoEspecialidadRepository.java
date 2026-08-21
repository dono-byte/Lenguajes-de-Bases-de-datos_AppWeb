package com.ufide.ProyectLenguajesBD.repository;

import com.ufide.ProyectLenguajesBD.entity.MedicoEspecialidad;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.entity.Especialidad;
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
public class MedicoEspecialidadRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall getMedicoEspecialidadCall;
    private final SimpleJdbcCall insMedicoEspecialidadCall;
    private final SimpleJdbcCall updMedicoEspecialidadCall;
    private final SimpleJdbcCall delMedicoEspecialidadCall;
    private final MedicoEspecialidadRowMapper rowMapper;

    public MedicoEspecialidadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new MedicoEspecialidadRowMapper();

        this.getMedicoEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("GET_MEDICO_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, rowMapper)
                );

        this.insMedicoEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("INS_MEDICO_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_fk_personal_medico", Types.INTEGER),
                        new SqlParameter("p_fk_especialidad", Types.INTEGER)
                );

        this.updMedicoEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("UPD_MEDICO_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER),
                        new SqlParameter("p_fk_personal_medico", Types.INTEGER),
                        new SqlParameter("p_fk_especialidad", Types.INTEGER)
                );

        this.delMedicoEspecialidadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CRUD_SISTEMA")
                .withProcedureName("DEL_MEDICO_ESPECIALIDAD")
                .declareParameters(
                        new SqlParameter("p_id", Types.INTEGER)
                );
    }

    private static class MedicoEspecialidadRowMapper implements RowMapper<MedicoEspecialidad> {
        @Override
        public MedicoEspecialidad mapRow(ResultSet rs, int rowNum) throws SQLException {
            MedicoEspecialidad me = new MedicoEspecialidad();
            me.setPkMedicoEspecialidad(rs.getInt("PK_MEDICO_ESPECIALIDAD"));
            int medicoId = rs.getInt("FK_PERSONAL_MEDICO");
            if (medicoId > 0) {
                PersonalMedico pm = new PersonalMedico();
                pm.setPkPersonalMedico(medicoId);
                me.setPersonalMedico(pm);
            }
            int especialidadId = rs.getInt("FK_ESPECIALIDAD");
            if (especialidadId > 0) {
                Especialidad e = new Especialidad();
                e.setPkEspecialidad(especialidadId);
                me.setEspecialidad(e);
            }
            return me;
        }
    }

    public Optional<MedicoEspecialidad> findById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        Map<String, Object> result = getMedicoEspecialidadCall.execute(params);
        @SuppressWarnings("unchecked")
        List<MedicoEspecialidad> list = (List<MedicoEspecialidad>) result.get("p_result");
        return list.stream().findFirst();
    }

    public List<MedicoEspecialidad> findAll() {
        String sql = "SELECT * FROM MEDICO_ESPECIALIDAD";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public MedicoEspecialidad save(MedicoEspecialidad me) {
        if (me.getPkMedicoEspecialidad() == null) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_fk_personal_medico", me.getPersonalMedico() != null ? me.getPersonalMedico().getPkPersonalMedico() : null)
                    .addValue("p_fk_especialidad", me.getEspecialidad() != null ? me.getEspecialidad().getPkEspecialidad() : null);
            insMedicoEspecialidadCall.execute(params);
            // Recuperar por médico y especialidad
            String sql = "SELECT PK_MEDICO_ESPECIALIDAD FROM MEDICO_ESPECIALIDAD WHERE FK_PERSONAL_MEDICO = ? AND FK_ESPECIALIDAD = ?";
            List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class,
                    me.getPersonalMedico().getPkPersonalMedico(),
                    me.getEspecialidad().getPkEspecialidad());
            if (!ids.isEmpty()) {
                me.setPkMedicoEspecialidad(ids.get(0));
            }
            return me;
        } else {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("p_id", me.getPkMedicoEspecialidad())
                    .addValue("p_fk_personal_medico", me.getPersonalMedico() != null ? me.getPersonalMedico().getPkPersonalMedico() : null)
                    .addValue("p_fk_especialidad", me.getEspecialidad() != null ? me.getEspecialidad().getPkEspecialidad() : null);
            updMedicoEspecialidadCall.execute(params);
            return me;
        }
    }

    public void deleteById(Integer id) {
        MapSqlParameterSource params = new MapSqlParameterSource("p_id", id);
        delMedicoEspecialidadCall.execute(params);
    }
}