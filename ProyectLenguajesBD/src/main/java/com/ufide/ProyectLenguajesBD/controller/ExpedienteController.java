package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.repository.PacienteRepository;
import com.ufide.ProyectLenguajesBD.service.ExpedienteService;

@Controller
public class ExpedienteController {

    @Autowired
    private ExpedienteService expedienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping({"/expediente", "/expediente.html"})
    public String verExpediente(@RequestParam(required = false) Integer pacienteId, Model model) {
        List<Expediente> expedientes = expedienteService.obtenerTodos();
        List<Paciente> pacientes = pacienteRepository.findAll();
        Paciente pacienteSeleccionado = pacienteId == null
                ? pacientes.stream().findFirst().orElse(null)
                : pacienteRepository.findById(pacienteId).orElse(null);
        Expediente expedienteSeleccionado = pacienteSeleccionado == null ? null
                : expedienteService.obtenerPorPaciente(pacienteSeleccionado.getPkPaciente()).orElse(null);
        model.addAttribute("expedientes", expedientes);
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("resumenPacientes", pacientes.stream().map(paciente -> {
            Expediente expediente = expedienteService.obtenerPorPaciente(paciente.getPkPaciente()).orElse(null);
            List<com.ufide.ProyectLenguajesBD.entity.Consulta> consultas = expediente == null || expediente.getConsultas() == null
                ? Collections.emptyList() : expediente.getConsultas();
            List<MedicamentoAsignado> medicamentos = consultas.stream()
                .filter(consulta -> consulta.getRecetas() != null)
                .flatMap(consulta -> consulta.getRecetas().stream())
                .filter(receta -> receta.getDetalleRecetas() != null)
                .flatMap(receta -> receta.getDetalleRecetas().stream())
                .map(detalle -> new MedicamentoAsignado(detalle.getMedicamento().getNombre(), detalle.getDosis(), detalle.getFrecuencia()))
                .toList();
            return new PacienteResumen(paciente.getPkPaciente(), paciente.getNombre(), paciente.getCedula(),
                paciente.getFechaNacimiento(), paciente.getGenero(), paciente.getTelefono(), paciente.getDireccion(),
                consultas, medicamentos);
        }).toList());
        model.addAttribute("pacienteSeleccionado", pacienteSeleccionado);
        model.addAttribute("expediente", expedienteSeleccionado);
        return "expediente";
    }

    public record PacienteResumen(Integer id, String nombre, String cedula, java.time.LocalDate fechaNacimiento,
            String genero, String telefono, String direccion,
            List<com.ufide.ProyectLenguajesBD.entity.Consulta> consultas,
            List<MedicamentoAsignado> medicamentos) {}

    public record MedicamentoAsignado(String nombre, String dosis, String frecuencia) {}
}
