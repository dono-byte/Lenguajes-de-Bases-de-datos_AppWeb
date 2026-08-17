package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CitasController {

    @GetMapping({"/citas", "/citas.html"})
    public String verCitas() {
        return "citas"; // plantilla src/main/resources/templates/citas.html
    }
}
