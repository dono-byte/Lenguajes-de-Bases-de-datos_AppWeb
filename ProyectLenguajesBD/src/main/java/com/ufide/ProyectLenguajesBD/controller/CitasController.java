package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.CitaService;

@Controller
public class CitasController {

    @Autowired
    private CitaService citaService;

    @GetMapping({"/citas", "/citas.html"})
    public String verCitas(Model model) {
        model.addAttribute("citas", citaService.obtenerTodos());
        return "citas";
    }
}
