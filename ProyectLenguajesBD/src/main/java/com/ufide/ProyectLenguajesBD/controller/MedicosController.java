package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ufide.ProyectLenguajesBD.service.PersonalMedicoService;
import com.ufide.ProyectLenguajesBD.entity.Especialidad;
import com.ufide.ProyectLenguajesBD.entity.MedicoEspecialidad;
import com.ufide.ProyectLenguajesBD.entity.PersonalMedico;
import com.ufide.ProyectLenguajesBD.entity.Usuario;
import com.ufide.ProyectLenguajesBD.repository.EspecialidadRepository;
import com.ufide.ProyectLenguajesBD.repository.MedicoEspecialidadRepository;
import com.ufide.ProyectLenguajesBD.repository.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class MedicosController {

    @Autowired
    private PersonalMedicoService personalMedicoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private MedicoEspecialidadRepository medicoEspecialidadRepository;

    @GetMapping({"/medicos", "/medicos.html"})
    public String verMedicos(Model model) {
        model.addAttribute("medicos", personalMedicoService.obtenerTodos());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("especialidades", especialidadRepository.findAll());
        return "medicos";
    }

    @GetMapping("/api/medicos")
    @ResponseBody
    public List<MedicoResponse> listarApi() {
        return personalMedicoService.obtenerTodos().stream().map(this::convertir).collect(Collectors.toList());
    }

    @PostMapping("/api/medicos")
    @ResponseBody
    @Transactional
    public MedicoResponse crearApi(@RequestBody MedicoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElseThrow();
        Especialidad especialidad = especialidadRepository.findById(request.especialidadId()).orElseThrow();
        PersonalMedico medico = new PersonalMedico(usuario, request.nombre(), request.apellido(), request.segApellido(),
                request.codigoMedico(), request.correoElectronico(), request.telefono(), request.estado());
        PersonalMedico guardado = personalMedicoService.guardar(medico);
        medicoEspecialidadRepository.save(new MedicoEspecialidad(guardado, especialidad));
        return convertir(guardado);
    }

    @PutMapping("/api/medicos/{id}")
    @ResponseBody
    @Transactional
    public MedicoResponse actualizarApi(@PathVariable Integer id, @RequestBody MedicoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElseThrow();
        Especialidad especialidad = especialidadRepository.findById(request.especialidadId()).orElseThrow();
        PersonalMedico medico = new PersonalMedico(null, request.nombre(), request.apellido(), request.segApellido(),
                request.codigoMedico(), request.correoElectronico(), request.telefono(), request.estado());
        PersonalMedico actualizado = personalMedicoService.actualizar(id, medico);
        if (actualizado == null) {
            throw new IllegalArgumentException("Médico no encontrado");
        }
        actualizado.setUsuario(usuario);
        personalMedicoService.guardar(actualizado);
        medicoEspecialidadRepository.deleteAll(medicoEspecialidadRepository.findAll().stream()
            .filter(relacion -> relacion.getPersonalMedico().getPkPersonalMedico().equals(id)).toList());
        medicoEspecialidadRepository.save(new MedicoEspecialidad(actualizado, especialidad));
        return convertir(actualizado);
    }

    @DeleteMapping("/api/medicos/{id}")
    @ResponseBody
    @Transactional
    public void eliminarApi(@PathVariable Integer id) {
        medicoEspecialidadRepository.deleteAll(medicoEspecialidadRepository.findAll().stream()
                .filter(relacion -> relacion.getPersonalMedico().getPkPersonalMedico().equals(id)).toList());
        personalMedicoService.eliminar(id);
    }

    private MedicoResponse convertir(PersonalMedico medico) {
        String especialidad = medico.getMedicoEspecialidades() == null ? "" : medico.getMedicoEspecialidades().stream()
            .map(relacion -> relacion.getEspecialidad().getNombre()).distinct().collect(Collectors.joining(", "));
        Integer especialidadId = medico.getMedicoEspecialidades() == null ? null : medico.getMedicoEspecialidades().stream()
            .map(relacion -> relacion.getEspecialidad().getPkEspecialidad()).findFirst().orElse(null);
        return new MedicoResponse(medico.getPkPersonalMedico(), medico.getUsuario().getPkUsuario(), medico.getCodigoMedico(),
            especialidadId, medico.getNombre(), medico.getApellido(), medico.getTelefono(),
            medico.getCorreoElectronico(), medico.getEstado(), especialidad);
    }

    public record MedicoRequest(Integer usuarioId, Integer especialidadId, String nombre, String apellido,
            String segApellido, String codigoMedico, String correoElectronico, String telefono, String estado) {}

        public record MedicoResponse(Integer id, Integer usuarioId, String codigoMedico, Integer especialidadId,
            String nombre, String apellido, String telefono, String correoElectronico, String estado, String especialidad) {}
}
