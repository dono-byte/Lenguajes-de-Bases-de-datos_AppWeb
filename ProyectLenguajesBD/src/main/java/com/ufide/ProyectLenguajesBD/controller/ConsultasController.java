package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.ConsultaService;

@Controller
public class ConsultasController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping({"/consultas", "/consultas.html"})
    public String verConsultas(Model model) {
        model.addAttribute("consultas", consultaService.obtenerTodos());
        return "consultas";
    }
}
