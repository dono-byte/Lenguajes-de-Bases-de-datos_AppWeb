package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;

@Controller
public class MedicosController {

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @GetMapping({"/medicos", "/medicos.html"})
    public String verMedicos(Model model) {
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        return "medicos";
    }
}
