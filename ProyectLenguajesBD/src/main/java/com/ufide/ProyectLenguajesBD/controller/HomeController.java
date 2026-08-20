package com.ufide.ProyectLenguajesBD.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.repository.CitaRepository;
import com.ufide.ProyectLenguajesBD.service.CitaService;
import com.ufide.ProyectLenguajesBD.service.ConsultaService;
import com.ufide.ProyectLenguajesBD.service.PacienteService;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;


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
    private CitaRepository citaRepository;

    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {

        //OBTIENE TODOS LOS DATOS
        List<Cita> todasLasCitas = citaService.obtenerTodos();

        //TOTAL DE LOS PACIENTES
        model.addAttribute("totalPacientes", pacienteService.obtenerTodos().size());

        //TOTAL DE LAS CONSULTAS
        model.addAttribute("totalConsultas", consultaService.obtenerTodos().size());

        //TOTAL DE LOS MEDICOS
        model.addAttribute("totalMedicos", personalMedicoService.obtenerTodos().size());

        //FECHA ACTUAL
        LocalDate hoy = LocalDate.now();

        //CITAS DE HOY (DE LA FECHA ACTUAL)
        LocalDateTime inicioHoy = hoy.atStartOfDay();
        LocalDateTime finHoy = hoy.plusDays(1).atStartOfDay();

        List<Cita> citasHoy = citaRepository.findByFechaHoraBetween(inicioHoy, finHoy);


        model.addAttribute("totalCitas", citasHoy.size());

        //LAS PROXIMAS CITAS DE LA SEMANA ACTUAL (LUNES A DOMINGO)
        LocalDateTime ahora = LocalDateTime.now();

        List<Cita> proximasCitas =
                todasLasCitas.stream()
                        .filter(cita -> cita.getFechaHora() != null)
                        .filter(cita -> cita.getFechaHora().isAfter(ahora))
                        .sorted(Comparator.comparing(Cita::getFechaHora))
                        .limit(5)
                        .toList();

        model.addAttribute("citas", proximasCitas);

        //INICIO DE LA SEMANA ACTUAL
        LocalDate inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        //CITAS POR DIA
        List<Integer> citasPorDia = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            //DIa que se esta calculando
            LocalDate dia = inicioSemana.plusDays(i);

            //Inicio del dia
            LocalDateTime inicioDia = dia.atStartOfDay();

            //Inicio del siguiente dia
            LocalDateTime finDia = dia.plusDays(1).atStartOfDay();

            //Busca las citas de ese dia en el Oracle
            List<Cita> citasDelDia = citaRepository.findByFechaHoraBetween(inicioDia, finDia);

            //Guarda la cantidad de citas 
            citasPorDia.add(citasDelDia.size());
        }

        //NOMBRES DE LOS DIAS
        List<String> etiquetasDias =
                List.of("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");

        //ENVIA LOS DATOS DE LA GRaFICA AL HTML
        model.addAttribute("etiquetasDias", etiquetasDias);
        model.addAttribute("citasPorDia", citasPorDia);

        //LISTA DE los PACIENTES
        model.addAttribute("pacientes", pacienteService.obtenerTodos());

        return "index";
    }
}