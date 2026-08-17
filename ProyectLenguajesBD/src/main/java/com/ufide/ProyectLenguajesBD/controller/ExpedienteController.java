package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExpedienteController {

    @GetMapping({"/expediente", "/expediente.html"})
    public String verExpediente() {
        return "expediente";
    }
}
