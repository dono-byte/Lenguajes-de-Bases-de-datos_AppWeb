package com.ufide.ProyectLenguajesBD.service;

import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReportesService {

    private final JdbcTemplate jdbcTemplate;

    // Llamadas a procedimientos con cursores
    private final SimpleJdbcCall resumenDashboardCall;
    private final SimpleJdbcCall citasPorDiaCall;
    private final SimpleJdbcCall expedienteCompletoCall;
    private final SimpleJdbcCall historialExpedienteCall;

    // Llamadas a procedimientos que solo imprimen en consola (no devuelven datos)
    private final SimpleJdbcCall agendaMedicoCall;
    private final SimpleJdbcCall reporteStockCall;
    private final SimpleJdbcCall emitirRecetaDetalleCall;

    public ReportesService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        // --- Procedimientos con cursores ---

        // 1. Resumen del dashboard
        this.resumenDashboardCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("OBTENER_RESUMEN_DASHBOARD")
                .declareParameters(
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR)
                );

        // 2. Citas por día de la semana
        this.citasPorDiaCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("OBTENER_CITAS_POR_DIA_SEMANA")
                .declareParameters(
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR)
                );

        // 3. Expediente completo de un paciente
        this.expedienteCompletoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("OBTENER_EXPEDIENTE_COMPLETO")
                .declareParameters(
                        new SqlParameter("p_paciente_id", Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR)
                );

        // 4. Historial del expediente (devuelve CLOB)
        this.historialExpedienteCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("HISTORIAL_EXPEDIENTE")
                .declareParameters(
                        new SqlParameter("p_cedula", Types.VARCHAR),
                        new SqlOutParameter("p_historial", OracleTypes.CLOB)
                );

        // --- Procedimientos que usan DBMS_OUTPUT (sin retorno) ---
        this.agendaMedicoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AGENDA_MEDICO")
                .declareParameters(
                        new SqlParameter("p_fk_personal_medico", Types.INTEGER),
                        new SqlParameter("p_fecha", Types.DATE)
                );

        this.reporteStockCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("REPORTE_STOCK_MEDICAMENTOS")
                .declareParameters(
                        new SqlParameter("p_umbral_stock", Types.INTEGER),
                        new SqlParameter("p_dias_venc", Types.INTEGER)
                );

        this.emitirRecetaDetalleCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("EMITIR_RECETA_DETALLE")
                .declareParameters(
                        new SqlParameter("p_fk_receta", Types.INTEGER)
                );
    }

    // ========================================================================
    // 1. OBTENER RESUMEN DEL DASHBOARD
    // ========================================================================
    public Map<String, Object> obtenerResumenDashboard() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        Map<String, Object> result = resumenDashboardCall.execute(params);
        // El cursor se devuelve en la clave "p_cursor"
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("p_cursor");
        if (list.isEmpty()) {
            return new HashMap<>();
        }
        return list.get(0);
    }

    // ========================================================================
    // 2. OBTENER CITAS POR DÍA DE LA SEMANA
    // ========================================================================
    public List<Map<String, Object>> obtenerCitasPorDiaSemana() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        Map<String, Object> result = citasPorDiaCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("p_cursor");
        return list;
    }

    // ========================================================================
    // 3. OBTENER EXPEDIENTE COMPLETO DE UN PACIENTE
    // ========================================================================
    public List<Map<String, Object>> obtenerExpedienteCompleto(Integer pacienteId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_paciente_id", pacienteId);
        Map<String, Object> result = expedienteCompletoCall.execute(params);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("p_cursor");
        return list;
    }

    // ========================================================================
    // 4. OBTENER HISTORIAL DEL EXPEDIENTE (CLOB)
    // ========================================================================
    public String obtenerHistorialExpediente(String cedula) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_cedula", cedula);
        Map<String, Object> result = historialExpedienteCall.execute(params);
        Clob clob = (Clob) result.get("p_historial");
        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer el CLOB del historial", e);
        }
    }

    // ========================================================================
    // 5. AGENDA DEL MÉDICO (imprime en consola, no devuelve datos)
    // ========================================================================
    public void ejecutarAgendaMedico(Integer medicoId, java.util.Date fecha) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_fk_personal_medico", medicoId)
                .addValue("p_fecha", new java.sql.Date(fecha.getTime()));
        agendaMedicoCall.execute(params);
        // La salida se escribe en DBMS_OUTPUT (no capturable directamente)
        // Para verla, debes habilitar la salida en el cliente SQL o modificar el procedimiento.
        System.out.println("Procedimiento AGENDA_MEDICO ejecutado. Revisa la salida en la consola de la base de datos.");
    }

    // ========================================================================
    // 6. REPORTE DE STOCK DE MEDICAMENTOS (imprime en consola)
    // ========================================================================
    public void ejecutarReporteStock(Integer umbral, Integer diasVenc) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_umbral_stock", umbral)
                .addValue("p_dias_venc", diasVenc);
        reporteStockCall.execute(params);
        System.out.println("Procedimiento REPORTE_STOCK_MEDICAMENTOS ejecutado. Revisa la salida en la consola de la base de datos.");
    }

    // ========================================================================
    // 7. EMITIR DETALLE DE RECETA (imprime en consola)
    // ========================================================================
    public void ejecutarEmitirRecetaDetalle(Integer recetaId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_fk_receta", recetaId);
        emitirRecetaDetalleCall.execute(params);
        System.out.println("Procedimiento EMITIR_RECETA_DETALLE ejecutado. Revisa la salida en la consola de la base de datos.");
    }

    // ========================================================================
    // MÉTODO AUXILIAR PARA CONSULTAR USANDO UN CURSOR GENÉRICO (opcional)
    // ========================================================================
    /**
     * Ejecuta cualquier procedimiento que devuelva un cursor con un solo parámetro de entrada.
     * Ejemplo: GET_CITAS_POR_PACIENTE, GET_CONSULTAS_POR_EXPEDIENTE, etc.
     * (Estos procedimientos ya están en los repositorios, pero este método puede ser útil para pruebas).
     */
    public List<Map<String, Object>> ejecutarProcedimientoCursor(String nombreProcedimiento,
                                                                 String paramNombre,
                                                                 Object paramValor) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(nombreProcedimiento)
                .declareParameters(
                        new SqlParameter(paramNombre, Types.INTEGER),
                        new SqlOutParameter("p_cursor", OracleTypes.CURSOR)
                );
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(paramNombre, paramValor);
        Map<String, Object> result = call.execute(params);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("p_cursor");
        return list;
    }
}