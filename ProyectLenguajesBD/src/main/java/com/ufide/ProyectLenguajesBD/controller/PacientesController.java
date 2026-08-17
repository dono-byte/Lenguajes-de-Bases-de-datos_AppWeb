package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PacientesController {

    @GetMapping({"/pacientes", "/pacientes.html"})
    public String verPacientes() {
        return "pacientes";
    }
}
