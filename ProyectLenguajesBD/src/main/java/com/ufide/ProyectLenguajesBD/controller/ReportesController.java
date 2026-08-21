package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    @GetMapping("/dashboard")
    public Map<String, Object> obtenerResumenDashboard() {
        return reportesService.obtenerResumenDashboard();
    }

    @GetMapping("/citas-por-dia")
    public List<Map<String, Object>> obtenerCitasPorDia() {
        return reportesService.obtenerCitasPorDiaSemana();
    }

    @GetMapping("/expediente-completo/{pacienteId}")
    public List<Map<String, Object>> obtenerExpedienteCompleto(@PathVariable Integer pacienteId) {
        return reportesService.obtenerExpedienteCompleto(pacienteId);
    }

    @GetMapping("/historial/{cedula}")
    public String obtenerHistorial(@PathVariable String cedula) {
        return reportesService.obtenerHistorialExpediente(cedula);
    }

    // Endpoints para procedimientos que solo imprimen en consola (no retornan datos)
    @PostMapping("/agenda-medico")
    public String ejecutarAgendaMedico(@RequestParam Integer medicoId, @RequestParam java.util.Date fecha) {
        reportesService.ejecutarAgendaMedico(medicoId, fecha);
        return "Procedimiento AGENDA_MEDICO ejecutado (consulta la consola de la base de datos).";
    }

    @PostMapping("/reporte-stock")
    public String ejecutarReporteStock(@RequestParam(defaultValue = "20") Integer umbral,
                                       @RequestParam(defaultValue = "90") Integer diasVenc) {
        reportesService.ejecutarReporteStock(umbral, diasVenc);
        return "Procedimiento REPORTE_STOCK_MEDICAMENTOS ejecutado.";
    }

    @PostMapping("/emitir-receta/{recetaId}")
    public String ejecutarEmitirReceta(@PathVariable Integer recetaId) {
        reportesService.ejecutarEmitirRecetaDetalle(recetaId);
        return "Procedimiento EMITIR_RECETA_DETALLE ejecutado.";
    }
}