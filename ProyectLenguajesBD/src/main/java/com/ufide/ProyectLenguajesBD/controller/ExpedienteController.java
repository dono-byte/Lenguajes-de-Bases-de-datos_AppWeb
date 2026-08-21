package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.entity.Receta;
import com.ufide.ProyectLenguajesBD.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ExpedienteController {

    @Autowired
    private ExpedienteService expedienteService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private DetalleRecetaService detalleRecetaService;

    @GetMapping({"/expediente", "/expediente.html"})
    public String verExpediente(@RequestParam(required = false) Integer pacienteId, Model model) {
        List<Paciente> pacientes = pacienteService.obtenerTodos();
        Paciente pacienteSeleccionado = pacienteId == null
                ? pacientes.stream().findFirst().orElse(null)
                : pacienteService.obtenerPorId(pacienteId).orElse(null);

        Expediente expedienteSeleccionado = pacienteSeleccionado == null ? null
                : expedienteService.obtenerPorPaciente(pacienteSeleccionado.getPkPaciente()).orElse(null);

        // Construir resumen para todos los pacientes
        List<PacienteResumen> resumenPacientes = pacientes.stream()
                .map(paciente -> {
                    Expediente exp = expedienteService.obtenerPorPaciente(paciente.getPkPaciente()).orElse(null);
                    List<Consulta> consultas = (exp == null) ? Collections.emptyList()
                            : consultaService.obtenerPorExpediente(exp.getPkExpediente());

                    // Cargar recetas y detalles para cada consulta
                    List<MedicamentoAsignado> medicamentos = new ArrayList<>();
                    for (Consulta cons : consultas) {
                        List<Receta> recetas = recetaService.obtenerPorConsulta(cons.getPkConsulta());
                        for (Receta receta : recetas) {
                            List<DetalleReceta> detalles = detalleRecetaService.obtenerPorReceta(receta.getPkReceta());
                            for (DetalleReceta detalle : detalles) {
                                medicamentos.add(new MedicamentoAsignado(
                                        detalle.getMedicamento().getNombre(),
                                        detalle.getDosis(),
                                        detalle.getFrecuencia()
                                ));
                            }
                        }
                    }

                    return new PacienteResumen(
                            paciente.getPkPaciente(),
                            paciente.getNombre(),
                            paciente.getCedula(),
                            paciente.getFechaNacimiento(),
                            paciente.getGenero(),
                            paciente.getTelefono(),
                            paciente.getDireccion(),
                            consultas,
                            medicamentos
                    );
                })
                .toList();

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("pacienteSeleccionado", pacienteSeleccionado);
        model.addAttribute("expediente", expedienteSeleccionado);
        model.addAttribute("resumenPacientes", resumenPacientes);

        return "expediente";
    }

    // --- DTOs internos ---
    public record PacienteResumen(Integer id, String nombre, String cedula, java.time.LocalDate fechaNacimiento,
                                  String genero, String telefono, String direccion,
                                  List<Consulta> consultas,
                                  List<MedicamentoAsignado> medicamentos) {}

    public record MedicamentoAsignado(String nombre, String dosis, String frecuencia) {}
}