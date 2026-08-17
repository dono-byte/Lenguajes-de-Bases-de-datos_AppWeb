package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedicamentosController {

    @GetMapping({"/medicamentos", "/medicamentos.html"})
    public String verMedicamentos() {
        return "medicamentos";
    }
}
