package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }
}
