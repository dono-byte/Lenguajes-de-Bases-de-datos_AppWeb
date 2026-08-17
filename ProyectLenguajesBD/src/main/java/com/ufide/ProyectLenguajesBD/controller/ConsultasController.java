package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultasController {

    @GetMapping({"/consultas", "/consultas.html"})
    public String verConsultas() {
        return "consultas";
    }
}
