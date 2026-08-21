package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.CitaService;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;
import com.ufide.ProyectLenguajesBD.entity.Cita;
import com.ufide.ProyectLenguajesBD.entity.Consultorio;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import com.ufide.ProyectLenguajesBD.repository.ConsultorioRepository;
import com.ufide.ProyectLenguajesBD.repository.PacienteRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CitasController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @GetMapping({"/citas", "/citas.html"})
    public String verCitas(Model model) {
        model.addAttribute("citas", citaService.obtenerTodos());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("consultorios", consultorioRepository.findAll());
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        return "citas";
    }

    @GetMapping("/api/citas")
    @ResponseBody
    public List<CitaResponse> listarApi() {
        return citaService.obtenerTodos().stream().map(this::convertir).collect(Collectors.toList());
    }

    @PostMapping("/api/citas")
    @ResponseBody
    public CitaResponse crearApi(@RequestBody CitaRequest request) {
        Cita cita = construir(request);
        return convertir(citaService.guardar(cita));
    }

    @PutMapping("/api/citas/{id}")
    @ResponseBody
    public CitaResponse actualizarApi(@PathVariable Integer id, @RequestBody CitaRequest request) {
        Cita existente = citaService.obtenerPorId(id).orElseThrow();
        existente.setPaciente(pacienteRepository.findById(request.pacienteId()).orElseThrow());
        existente.setConsultorio(consultorioRepository.findById(request.consultorioId()).orElseThrow());
        existente.setFechaHora(request.fechaHora());
        existente.setDuracion(request.duracion());
        existente.setEstado(request.estado());
        return convertir(citaService.guardar(existente));
    }

    @DeleteMapping("/api/citas/{id}")
    @ResponseBody
    public void eliminarApi(@PathVariable Integer id) {
        citaService.eliminar(id);
    }

    private Cita construir(CitaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId()).orElseThrow();
        Consultorio consultorio = consultorioRepository.findById(request.consultorioId()).orElseThrow();
        return new Cita(paciente, consultorio, request.fechaHora(), request.duracion(), request.estado());
    }

    private CitaResponse convertir(Cita cita) {
        return new CitaResponse(cita.getPkCita(), cita.getPaciente().getPkPaciente(), cita.getPaciente().getNombre(),
                cita.getConsultorio().getPkConsultorio(), cita.getConsultorio().getNumeroConsultorio(),
                cita.getConsultorio().getLocalidad(), cita.getConsultorio().getProvincia(), cita.getFechaHora(),
                cita.getDuracion(), cita.getEstado());
    }

    public record CitaRequest(Integer pacienteId, Integer consultorioId, LocalDateTime fechaHora,
            String duracion, String estado) {}

    public record CitaResponse(Integer id, Integer pacienteId, String paciente, Integer consultorioId,
            String consultorio, String localidad, String provincia, LocalDateTime fechaHora, String duracion, String estado) {}
}
