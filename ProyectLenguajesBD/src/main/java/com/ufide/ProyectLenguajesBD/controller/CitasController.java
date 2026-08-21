package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.entity.Consultorio;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.service.CitaService;
import com.ufide.ProyectLenguajesBD.service.ConsultorioService;
import com.ufide.ProyectLenguajesBD.service.PacienteService;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CitasController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultorioService consultorioService;

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @GetMapping({"/citas", "/citas.html"})
    public String verCitas(Model model) {
        model.addAttribute("citas", citaService.obtenerTodos());
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("consultorios", consultorioService.obtenerTodos());
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        return "citas";
    }

    @GetMapping("/api/citas")
    @ResponseBody
    public List<CitaResponse> listarApi() {
        return citaService.obtenerTodos().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/citas")
    @ResponseBody
    public CitaResponse crearApi(@RequestBody CitaRequest request) {
        Paciente paciente = pacienteService.obtenerPorId(request.pacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        Consultorio consultorio = consultorioService.obtenerPorId(request.consultorioId())
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado"));
        Cita cita = new Cita(paciente, consultorio, request.fechaHora(), request.duracion(), request.estado());
        return convertir(citaService.guardar(cita));
    }

    @PutMapping("/api/citas/{id}")
    @ResponseBody
    public CitaResponse actualizarApi(@PathVariable Integer id, @RequestBody CitaRequest request) {
        Paciente paciente = pacienteService.obtenerPorId(request.pacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        Consultorio consultorio = consultorioService.obtenerPorId(request.consultorioId())
                .orElseThrow(() -> new IllegalArgumentException("Consultorio no encontrado"));
        Cita citaExistente = citaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        citaExistente.setPaciente(paciente);
        citaExistente.setConsultorio(consultorio);
        citaExistente.setFechaHora(request.fechaHora());
        citaExistente.setDuracion(request.duracion());
        citaExistente.setEstado(request.estado());
        return convertir(citaService.guardar(citaExistente));
    }

    @DeleteMapping("/api/citas/{id}")
    @ResponseBody
    public void eliminarApi(@PathVariable Integer id) {
        citaService.eliminar(id);
    }

    // --- DTOs y conversiones ---
    public record CitaRequest(Integer pacienteId, Integer consultorioId, LocalDateTime fechaHora,
                              String duracion, String estado) {}

    public record CitaResponse(Integer id, Integer pacienteId, String paciente,
                               Integer consultorioId, String consultorio,
                               String localidad, String provincia,
                               LocalDateTime fechaHora, String duracion, String estado) {}

    private CitaResponse convertir(Cita cita) {
        return new CitaResponse(
                cita.getPkCita(),
                cita.getPaciente().getPkPaciente(),
                cita.getPaciente().getNombre(),
                cita.getConsultorio().getPkConsultorio(),
                cita.getConsultorio().getNumeroConsultorio(),
                cita.getConsultorio().getLocalidad(),
                cita.getConsultorio().getProvincia(),
                cita.getFechaHora(),
                cita.getDuracion(),
                cita.getEstado()
        );
    }
}