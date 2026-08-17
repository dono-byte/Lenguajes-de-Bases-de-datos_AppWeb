package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedicosController {

    @GetMapping({"/medicos", "/medicos.html"})
    public String verMedicos() {
        return "medicos";
    }
}
