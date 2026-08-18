package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.PacienteService;
import com.ufide.ProyectLenguajesBD.service.CitaService;
import com.ufide.ProyectLenguajesBD.service.ConsultaService;
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

    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {
        model.addAttribute("totalPacientes", pacienteService.obtenerTodos().size());
        model.addAttribute("totalCitas", citaService.obtenerTodos().size());
        model.addAttribute("totalConsultas", consultaService.obtenerTodos().size());
        model.addAttribute("totalMedicos", personalMedicoService.obtenerTodos().size());
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("citas", citaService.obtenerTodos());
        return "index";
    }
}
