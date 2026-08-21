package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.entity.Diagnostico;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ConsultasController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @Autowired
    private DiagnosticoService diagnosticoService;

    @Autowired
    private ExpedienteService expedienteService;

    @GetMapping({"/consultas", "/consultas.html"})
    public String verConsultas(Model model) {
        model.addAttribute("consultas", consultaService.obtenerTodos());
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("citas", citaService.obtenerTodos());
        model.addAttribute("diagnosticos", diagnosticoService.obtenerTodos());
        return "consultas";
    }

    @GetMapping("/api/consultas")
    @ResponseBody
    public List<ConsultaResponse> listarApi() {
        return consultaService.obtenerTodos().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/consultas")
    @ResponseBody
    @Transactional
    public ConsultaResponse crearApi(@RequestBody ConsultaRequest request) {
        Consulta consulta = construirConsulta(request);
        return convertir(consultaService.guardar(consulta));
    }

    @PutMapping("/api/consultas/{id}")
    @ResponseBody
    @Transactional
    public ConsultaResponse actualizarApi(@PathVariable Integer id, @RequestBody ConsultaRequest request) {
        Consulta consulta = consultaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));
        actualizarConsulta(consulta, request);
        return convertir(consultaService.guardar(consulta));
    }

    @DeleteMapping("/api/consultas/{id}")
    @ResponseBody
    public void eliminarApi(@PathVariable Integer id) {
        consultaService.eliminar(id);
    }

    // --- Métodos auxiliares ---
    private Consulta construirConsulta(ConsultaRequest request) {
        Consulta consulta = new Consulta();
        actualizarConsulta(consulta, request);
        return consulta;
    }

    private void actualizarConsulta(Consulta consulta, ConsultaRequest request) {
        Paciente paciente = pacienteService.obtenerPorId(request.pacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        // Obtener o crear expediente
        Expediente expediente = expedienteService.obtenerPorPaciente(paciente.getPkPaciente())
                .orElseGet(() -> {
                    Expediente nuevo = new Expediente(paciente, LocalDate.now());
                    return expedienteService.guardar(nuevo);
                });
        consulta.setExpediente(expediente);

        PersonalMedico medico = personalMedicoService.obtenerPorId(request.medicoId())
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));
        consulta.setPersonalMedico(medico);

        Cita cita = citaService.obtenerPorId(request.citaId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        if (!cita.getPaciente().getPkPaciente().equals(paciente.getPkPaciente())) {
            throw new IllegalArgumentException("La cita no pertenece al paciente seleccionado");
        }
        consulta.setCita(cita);

        if (request.diagnosticoId() != null) {
            Diagnostico diagnostico = diagnosticoService.obtenerPorId(request.diagnosticoId())
                    .orElseThrow(() -> new IllegalArgumentException("Diagnóstico no encontrado"));
            consulta.setDiagnostico(diagnostico);
        } else {
            consulta.setDiagnostico(null);
        }

        consulta.setFechaConsulta(request.fechaConsulta());
        consulta.setMotivo(request.motivo());
        consulta.setObservaciones(request.observaciones());
    }

    // --- DTOs y conversión ---
    public record ConsultaRequest(Integer pacienteId, Integer citaId, Integer medicoId,
                                  Integer diagnosticoId, LocalDate fechaConsulta,
                                  String motivo, String observaciones) {}

    public record ConsultaResponse(Integer id, LocalDate fechaConsulta,
                                   Integer pacienteId, String paciente,
                                   Integer citaId, Integer medicoId, String medico,
                                   Integer diagnosticoId, String diagnostico,
                                   String motivo, String observaciones) {}

    private ConsultaResponse convertir(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getPkConsulta(),
                consulta.getFechaConsulta(),
                consulta.getExpediente().getPaciente().getPkPaciente(),
                consulta.getExpediente().getPaciente().getNombre(),
                consulta.getCita().getPkCita(),
                consulta.getPersonalMedico().getPkPersonalMedico(),
                consulta.getPersonalMedico().getNombre() + " " + consulta.getPersonalMedico().getApellido(),
                consulta.getDiagnostico() != null ? consulta.getDiagnostico().getPkDiagnostico() : null,
                consulta.getDiagnostico() != null ? consulta.getDiagnostico().getDescripcion() : "",
                consulta.getMotivo(),
                consulta.getObservaciones()
        );
    }
}