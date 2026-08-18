package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.ExpedienteService;

@Controller
public class ExpedienteController {

    @Autowired
    private ExpedienteService expedienteService;

    @GetMapping({"/expediente", "/expediente.html"})
    public String verExpediente(Model model) {
        model.addAttribute("expedientes", expedienteService.obtenerTodos());
        return "expediente";
    }
}
