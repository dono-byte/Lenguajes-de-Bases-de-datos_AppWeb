package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ufide.ProyectLenguajesBD.service.LoginService;
/**
 * Controlador de Autenticación.
 * Mapea las rutas para mostrar el formulario y procesar el inicio de sesión.
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * GET /login
     * Muestra la pantalla del formulario de inicio de sesión.
     */
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Devuelve la plantilla login.html
    }

    /**
     * POST /login
     * Procesa los datos enviados por el usuario al hacer clic en "Ingresar".
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario, 
                                @RequestParam String contrasena, 
                                Model modelo) {
        
        try {
            // 1. Llamamos a la lógica de negocio en el servicio
            boolean exito = loginService.iniciarSesion(usuario, contrasena);

            if (exito) {
                // Si los datos son correctos, lo redirigimos a la pantalla de inicio
                // Usamos "redirect:" para cambiar la URL del navegador
                return "redirect:/?nombre=" + usuario; 
            } else {
                // Si la clave no coincide, volvemos a mostrar el login con un mensaje
                modelo.addAttribute("error", "Usuario o contraseña incorrectos.");
                return "login";
            }

        } catch (IllegalStateException e) {
            // Captura la regla de negocio: si la cuenta está INACTIVA o BLOQUEADA
            modelo.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}