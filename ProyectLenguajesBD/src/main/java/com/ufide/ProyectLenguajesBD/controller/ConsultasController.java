package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ufide.ProyectLenguajesBD.service.ConsultaService;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;
import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.repository.DiagnosticoRepository;
import com.ufide.ProyectLenguajesBD.repository.ExpedienteRepository;
import com.ufide.ProyectLenguajesBD.repository.PersonalMedicoRepository;
import com.ufide.ProyectLenguajesBD.repository.PacienteRepository;
import com.ufide.ProyectLenguajesBD.repository.CitaRepository;
import com.ufide.ProyectLenguajesBD.entity.Expediente;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ConsultasController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @Autowired
    private ExpedienteRepository expedienteRepository;

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Autowired
    private PersonalMedicoRepository personalMedicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @GetMapping({"/consultas", "/consultas.html"})
    public String verConsultas(Model model) {
        model.addAttribute("consultas", consultaService.obtenerTodos());
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("citas", citaRepository.findAll());
        model.addAttribute("diagnosticos", diagnosticoRepository.findAll());
        return "consultas";
    }

    @GetMapping("/api/consultas")
    @ResponseBody
    public List<ConsultaResponse> listarApi() {
        return consultaService.obtenerTodos().stream().map(this::convertir).collect(Collectors.toList());
    }

    @PostMapping("/api/consultas")
    @ResponseBody
    @Transactional
    public ConsultaResponse crearApi(@RequestBody ConsultaRequest request) {
        Consulta consulta = construir(request);
        return convertir(consultaService.guardar(consulta));
    }

    @PutMapping("/api/consultas/{id}")
    @ResponseBody
    @Transactional
    public ConsultaResponse actualizarApi(@PathVariable Integer id, @RequestBody ConsultaRequest request) {
        Consulta consulta = consultaService.obtenerPorId(id).orElseThrow();
        aplicar(consulta, request);
        return convertir(consultaService.guardar(consulta));
    }

    @DeleteMapping("/api/consultas/{id}")
    @ResponseBody
    public void eliminarApi(@PathVariable Integer id) {
        consultaService.eliminar(id);
    }

    private Consulta construir(ConsultaRequest request) {
        Consulta consulta = new Consulta();
        aplicar(consulta, request);
        return consulta;
    }

    private void aplicar(Consulta consulta, ConsultaRequest request) {
        Expediente expediente = expedienteRepository.findByPacientePkPaciente(request.pacienteId())
            .orElseGet(() -> {
                Expediente nuevo = new Expediente(pacienteRepository.findById(request.pacienteId()).orElseThrow(), LocalDate.now());
                return expedienteRepository.save(nuevo);
            });
        consulta.setExpediente(expediente);
        consulta.setPersonalMedico(personalMedicoRepository.findById(request.medicoId()).orElseThrow());
        var cita = citaRepository.findById(request.citaId()).orElseThrow();
        if (!cita.getPaciente().getPkPaciente().equals(request.pacienteId())) {
            throw new IllegalArgumentException("La cita no pertenece al paciente seleccionado");
        }
        consulta.setCita(cita);
        consulta.setDiagnostico(request.diagnosticoId() == null ? null : diagnosticoRepository.findById(request.diagnosticoId()).orElseThrow());
        consulta.setFechaConsulta(request.fechaConsulta());
        consulta.setMotivo(request.motivo());
        consulta.setObservaciones(request.observaciones());
    }

    private ConsultaResponse convertir(Consulta consulta) {
        return new ConsultaResponse(consulta.getPkConsulta(), consulta.getFechaConsulta(),
                consulta.getExpediente().getPaciente().getPkPaciente(), consulta.getExpediente().getPaciente().getNombre(),
                consulta.getCita().getPkCita(), consulta.getPersonalMedico().getPkPersonalMedico(),
                consulta.getPersonalMedico().getNombre() + " " + consulta.getPersonalMedico().getApellido(),
                consulta.getDiagnostico() == null ? null : consulta.getDiagnostico().getPkDiagnostico(),
                consulta.getDiagnostico() == null ? "" : consulta.getDiagnostico().getDescripcion(), consulta.getMotivo(),
                consulta.getObservaciones());
    }

        public record ConsultaRequest(Integer pacienteId, Integer citaId, Integer medicoId, Integer diagnosticoId,
            LocalDate fechaConsulta, String motivo, String observaciones) {}

        public record ConsultaResponse(Integer id, LocalDate fechaConsulta, Integer pacienteId, String paciente,
            Integer citaId, Integer medicoId, String medico, Integer diagnosticoId, String diagnostico, String motivo,
            String observaciones) {}
}
