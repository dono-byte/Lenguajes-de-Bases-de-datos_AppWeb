package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.Especialidad;
import com.ufide.ProyectLenguajesBD.entity.MedicoEspecialidad;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
import com.ufide.ProyectLenguajesBD.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MedicosController {

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private MedicoEspecialidadService medicoEspecialidadService;

    @GetMapping({"/medicos", "/medicos.html"})
    public String verMedicos(Model model) {
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        return "medicos";
    }

    @GetMapping("/api/medicos")
    @ResponseBody
    public List<MedicoResponse> listarApi() {
        return personalMedicoService.obtenerTodos().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/medicos")
    @ResponseBody
    @Transactional
    public MedicoResponse crearApi(@RequestBody MedicoRequest request) {
        Usuario usuario = usuarioService.obtenerPorId(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Especialidad especialidad = especialidadService.obtenerPorId(request.especialidadId())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        PersonalMedico medico = new PersonalMedico(
                usuario,
                request.nombre(),
                request.apellido(),
                request.segApellido(),
                request.codigoMedico(),
                request.correoElectronico(),
                request.telefono(),
                request.estado()
        );
        PersonalMedico guardado = personalMedicoService.guardar(medico);

        // Asociar especialidad
        MedicoEspecialidad relacion = new MedicoEspecialidad(guardado, especialidad);
        medicoEspecialidadService.guardar(relacion);

        return convertir(guardado);
    }

    @PutMapping("/api/medicos/{id}")
    @ResponseBody
    @Transactional
    public MedicoResponse actualizarApi(@PathVariable Integer id, @RequestBody MedicoRequest request) {
        Usuario usuario = usuarioService.obtenerPorId(request.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Especialidad especialidad = especialidadService.obtenerPorId(request.especialidadId())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        PersonalMedico medico = personalMedicoService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));
        medico.setUsuario(usuario);
        medico.setNombre(request.nombre());
        medico.setApellido(request.apellido());
        medico.setSegApellido(request.segApellido());
        medico.setCodigoMedico(request.codigoMedico());
        medico.setCorreoElectronico(request.correoElectronico());
        medico.setTelefono(request.telefono());
        medico.setEstado(request.estado());

        PersonalMedico actualizado = personalMedicoService.guardar(medico);

        // Actualizar especialidad: eliminar todas las relaciones y crear la nueva
        List<MedicoEspecialidad> relacionesExistentes = medicoEspecialidadService.obtenerTodos().stream()
                .filter(rel -> rel.getPersonalMedico().getPkPersonalMedico().equals(id))
                .collect(Collectors.toList());
        for (MedicoEspecialidad rel : relacionesExistentes) {
            medicoEspecialidadService.eliminar(rel.getPkMedicoEspecialidad());
        }
        MedicoEspecialidad nuevaRel = new MedicoEspecialidad(actualizado, especialidad);
        medicoEspecialidadService.guardar(nuevaRel);

        return convertir(actualizado);
    }

    @DeleteMapping("/api/medicos/{id}")
    @ResponseBody
    @Transactional
    public void eliminarApi(@PathVariable Integer id) {
        // Eliminar relaciones
        List<MedicoEspecialidad> relaciones = medicoEspecialidadService.obtenerTodos().stream()
                .filter(rel -> rel.getPersonalMedico().getPkPersonalMedico().equals(id))
                .collect(Collectors.toList());
        for (MedicoEspecialidad rel : relaciones) {
            medicoEspecialidadService.eliminar(rel.getPkMedicoEspecialidad());
        }
        personalMedicoService.eliminar(id);
    }

    // --- Conversiones y DTOs ---
    private MedicoResponse convertir(PersonalMedico medico) {
        String especialidades = medico.getMedicoEspecialidades() != null
                ? medico.getMedicoEspecialidades().stream()
                .map(rel -> rel.getEspecialidad().getNombre())
                .distinct()
                .collect(Collectors.joining(", "))
                : "";
        Integer especialidadId = medico.getMedicoEspecialidades() != null && !medico.getMedicoEspecialidades().isEmpty()
                ? medico.getMedicoEspecialidades().get(0).getEspecialidad().getPkEspecialidad()
                : null;
        return new MedicoResponse(
                medico.getPkPersonalMedico(),
                medico.getUsuario().getPkUsuario(),
                medico.getCodigoMedico(),
                especialidadId,
                medico.getNombre(),
                medico.getApellido(),
                medico.getTelefono(),
                medico.getCorreoElectronico(),
                medico.getEstado(),
                especialidades
        );
    }

    public record MedicoRequest(Integer usuarioId, Integer especialidadId, String nombre, String apellido,
                                String segApellido, String codigoMedico, String correoElectronico,
                                String telefono, String estado) {}

    public record MedicoResponse(Integer id, Integer usuarioId, String codigoMedico, Integer especialidadId,
                                 String nombre, String apellido, String telefono,
                                 String correoElectronico, String estado, String especialidad) {}
}