package com.ufide.ProyectLenguajesBD.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e) {
        e.printStackTrace(); // Esto mostrará el error en la consola de Spring Boot
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error en el servidor: " + e.getMessage());
    }
}