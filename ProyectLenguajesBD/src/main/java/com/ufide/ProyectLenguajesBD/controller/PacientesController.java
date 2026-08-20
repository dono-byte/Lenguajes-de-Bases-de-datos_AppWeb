package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import com.ufide.ProyectLenguajesBD.service.PacienteService;
import com.ufide.ProyectLenguajesBD.entity.Paciente;
import java.time.LocalDate;

@Controller
public class PacientesController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping({"/pacientes", "/pacientes.html"})
    public String verPacientes(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "pacientes";
    }

    @PostMapping("/pacientes/crear")
    public String crearPaciente(
            @RequestParam String cedula,
            @RequestParam String nombre,
            @RequestParam LocalDate fechaNacimiento,
            @RequestParam String genero,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            Model model) {
        Paciente paciente = new Paciente(cedula, nombre, fechaNacimiento, genero, telefono, direccion);
        pacienteService.guardar(paciente);
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "redirect:/pacientes";
    }

    @GetMapping("/pacientes/editar/{id}")
    public String editarPaciente(@PathVariable Integer id, Model model) {
        model.addAttribute("paciente", pacienteService.obtenerPorId(id).orElse(null));
        return "pacientes";
    }

    @GetMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Integer id) {
        pacienteService.eliminar(id);
        return "redirect:/pacientes";
    }

    @GetMapping("/api/pacientes")
    @ResponseBody
    public List<PacienteResponse> listarApi() {
        return pacienteService.obtenerTodos().stream().map(this::convertir).toList();
    }

    @PostMapping("/api/pacientes")
    @ResponseBody
    public PacienteResponse crearApi(@RequestBody PacienteRequest request) {
        Paciente guardado = pacienteService.guardar(new Paciente(request.cedula(), request.nombre(), request.fechaNacimiento(),
                request.genero(), request.telefono(), request.direccion()));
        return convertir(guardado);
    }

    @PutMapping("/api/pacientes/{id}")
    @ResponseBody
    public PacienteResponse actualizarApi(@PathVariable Integer id, @RequestBody PacienteRequest request) {
        Paciente paciente = new Paciente(request.cedula(), request.nombre(), request.fechaNacimiento(),
                request.genero(), request.telefono(), request.direccion());
        Paciente actualizado = pacienteService.actualizar(id, paciente);
        if (actualizado == null) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        return convertir(actualizado);
    }

    @DeleteMapping("/api/pacientes/{id}")
    @ResponseBody
    public void eliminarApi(@PathVariable Integer id) {
        pacienteService.eliminar(id);
    }

    public record PacienteRequest(String cedula, String nombre, LocalDate fechaNacimiento,
            String genero, String telefono, String direccion) {}

        public record PacienteResponse(Integer id, String cedula, String nombre, LocalDate fechaNacimiento,
            String genero, String telefono, String direccion) {}

        private PacienteResponse convertir(Paciente paciente) {
        return new PacienteResponse(paciente.getPkPaciente(), paciente.getCedula(), paciente.getNombre(),
            paciente.getFechaNacimiento(), paciente.getGenero(), paciente.getTelefono(), paciente.getDireccion());
        }
}
