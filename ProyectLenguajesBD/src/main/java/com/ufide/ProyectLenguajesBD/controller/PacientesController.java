package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ufide.ProyectLenguajesBD.service.PacienteService;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import java.time.LocalDate;

@Controller
public class PacientesController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping({"/pacientes", "/pacientes.html"})
    public String verPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "pacientes";
    }

    @PostMapping("/pacientes/crear")
    public String crearPaciente(
            @RequestParam String cedula,
            @RequestParam String nombre,
            @RequestParam LocalDate fechaNacimiento,
            @RequestParam String genero,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            Model model) {
        Paciente paciente = new Paciente(cedula, nombre, fechaNacimiento, genero, telefono, direccion);
        pacienteService.guardar(paciente);
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "redirect:/pacientes";
    }

    @GetMapping("/pacientes/editar/{id}")
    public String editarPaciente(@RequestParam Integer id, Model model) {
        model.addAttribute("paciente", pacienteService.obtenerPorId(id).orElse(null));
        return "pacientes";
    }

    @GetMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@RequestParam Integer id) {
        pacienteService.eliminar(id);
        return "redirect:/pacientes";
    }
}
