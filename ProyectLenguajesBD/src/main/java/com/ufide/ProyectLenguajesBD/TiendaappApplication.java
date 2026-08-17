package com.ufide.ProyectLenguajesBD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * @SpringBootApplication escanea este paquete y todos los sub-paquetes
 * buscando @Controller, @Service, @Repository, @Entity, etc.
 */
@SpringBootApplication
public class TiendaappApplication {

    public static void main(String[] args) {
        System.setProperty("oracle.net.tns_admin", "C:/Wallet_BD01");
                SpringApplication.run(TiendaappApplication.class, args);
    }
}
