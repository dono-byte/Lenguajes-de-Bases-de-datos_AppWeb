package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.MedicamentoService;

@Controller
public class MedicamentosController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping({"/medicamentos", "/medicamentos.html"})
    public String verMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoService.obtenerTodos());
        return "medicamentos";
    }
}
