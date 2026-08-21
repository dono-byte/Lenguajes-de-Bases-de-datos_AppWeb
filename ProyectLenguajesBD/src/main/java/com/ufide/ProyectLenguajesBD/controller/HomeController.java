package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @Autowired
    private ReportesService reportesService;

    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {
        // Totales
        model.addAttribute("totalPacientes", pacienteService.obtenerTodos().size());
        model.addAttribute("totalConsultas", consultaService.obtenerTodos().size());
        model.addAttribute("totalMedicos", personalMedicoService.obtenerTodos().size());

        // Citas de hoy
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicioHoy = hoy.atStartOfDay();
        LocalDateTime finHoy = hoy.plusDays(1).atStartOfDay();
        List<Cita> citasHoy = citaService.obtenerPorRangoFecha(inicioHoy, finHoy);
        model.addAttribute("totalCitas", citasHoy.size());

        // Próximas citas (5)
        List<Cita> proximasCitas = citaService.obtenerTodos().stream()
                .filter(c -> c.getFechaHora() != null && c.getFechaHora().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Cita::getFechaHora))
                .limit(5)
                .toList();
        model.addAttribute("citas", proximasCitas);

        // Gráfico: citas por día de la semana (usando ReportesService)
        List<Map<String, Object>> citasPorDiaData = reportesService.obtenerCitasPorDiaSemana();
        List<String> etiquetasDias = List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        List<Integer> citasPorDia = new ArrayList<>();
        // Inicializar con ceros
        for (int i = 0; i < 7; i++) citasPorDia.add(0);
        // Llenar con datos del reporte (asumiendo que el procedimiento devuelve día y cantidad)
        for (Map<String, Object> row : citasPorDiaData) {
            String dia = row.get("DIA_SEMANA").toString().trim();
            int index = etiquetasDias.indexOf(dia);
            if (index >= 0) {
                citasPorDia.set(index, ((Number) row.get("CANTIDAD")).intValue());
            }
        }
        model.addAttribute("etiquetasDias", etiquetasDias);
        model.addAttribute("citasPorDia", citasPorDia);

        // Resumen del dashboard (opcional)
        Map<String, Object> resumen = reportesService.obtenerResumenDashboard();
        model.addAttribute("resumen", resumen);

        return "index";
    }
}