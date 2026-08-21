package com.ufide.ProyectLenguajesBD.controller;

import com.ufide.ProyectLenguajesBD.entity.*;
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
public class MedicamentosController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private DetalleMedicamentoService detalleMedicamentoService;

    @Autowired
    private DetalleRecetaService detalleRecetaService;

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private CitaService citaService; // no usado directamente, pero puede ser útil

    @GetMapping({"/medicamentos", "/medicamentos.html"})
    public String verMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoService.obtenerTodos());
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("consultas", consultaService.obtenerTodos());
        return "medicamentos";
    }

    @GetMapping("/api/medicamentos")
    @ResponseBody
    public List<MedicamentoResponse> listarMedicamentos() {
        return medicamentoService.obtenerTodos().stream()
                .map(this::convertirMedicamento)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/medicamentos")
    @ResponseBody
    @Transactional
    public MedicamentoResponse crearMedicamento(@RequestBody MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento(request.nombre());
        DetalleMedicamento detalle = construirDetalle(request);
        Medicamento guardado = medicamentoService.guardarConDetalle(medicamento, detalle);
        return convertirMedicamento(guardado);
    }

    @PutMapping("/api/medicamentos/{id}")
    @ResponseBody
    @Transactional
    public MedicamentoResponse actualizarMedicamento(@PathVariable Integer id, @RequestBody MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento(request.nombre());
        DetalleMedicamento detalle = construirDetalle(request);
        return medicamentoService.actualizarConDetalle(id, medicamento, detalle)
                .map(this::convertirMedicamento)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
    }

    @DeleteMapping("/api/medicamentos/{id}")
    @ResponseBody
    @Transactional
    public void eliminarMedicamento(@PathVariable Integer id) {
        medicamentoService.eliminar(id);
    }

    // --- Asignaciones de medicamentos a recetas ---
    @GetMapping("/api/asignaciones-medicamentos")
    @ResponseBody
    public List<AsignacionResponse> listarAsignaciones() {
        return detalleRecetaService.obtenerTodos().stream()
                .map(this::convertirAsignacion)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/asignaciones-medicamentos")
    @ResponseBody
    @Transactional
    public AsignacionResponse asignarMedicamento(@RequestBody AsignacionRequest request) {
        Consulta consulta = consultaService.obtenerPorId(request.consultaId())
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));
        if (!consulta.getExpediente().getPaciente().getPkPaciente().equals(request.pacienteId())) {
            throw new IllegalArgumentException("La consulta no pertenece al paciente seleccionado");
        }
        // Buscar receta existente o crear una nueva
        Receta receta = recetaService.obtenerPorConsulta(consulta.getPkConsulta()).stream().findFirst()
                .orElseGet(() -> {
                    Receta nueva = new Receta(consulta, LocalDate.now());
                    return recetaService.guardar(nueva);
                });
        Medicamento medicamento = medicamentoService.obtenerPorId(request.medicamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
        DetalleReceta detalle = new DetalleReceta(receta, medicamento, request.dosis(), request.frecuencia());
        DetalleReceta guardado = detalleRecetaService.guardar(detalle);
        return convertirAsignacion(guardado);
    }

    @PutMapping("/api/asignaciones-medicamentos/{id}")
    @ResponseBody
    @Transactional
    public AsignacionResponse actualizarAsignacion(@PathVariable Integer id, @RequestBody AsignacionRequest request) {
        DetalleReceta detalle = detalleRecetaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));
        Consulta consulta = consultaService.obtenerPorId(request.consultaId())
                .orElseThrow(() -> new IllegalArgumentException("Consulta no encontrada"));
        if (!consulta.getExpediente().getPaciente().getPkPaciente().equals(request.pacienteId())) {
            throw new IllegalArgumentException("La consulta no pertenece al paciente seleccionado");
        }
        // Actualizar receta (si cambia la consulta, se asocia a otra receta)
        Receta receta = recetaService.obtenerPorConsulta(consulta.getPkConsulta()).stream().findFirst()
                .orElseGet(() -> {
                    Receta nueva = new Receta(consulta, LocalDate.now());
                    return recetaService.guardar(nueva);
                });
        detalle.setReceta(receta);
        Medicamento medicamento = medicamentoService.obtenerPorId(request.medicamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
        detalle.setMedicamento(medicamento);
        detalle.setDosis(request.dosis());
        detalle.setFrecuencia(request.frecuencia());
        DetalleReceta actualizado = detalleRecetaService.guardar(detalle);
        return convertirAsignacion(actualizado);
    }

    @DeleteMapping("/api/asignaciones-medicamentos/{id}")
    @ResponseBody
    @Transactional
    public void eliminarAsignacion(@PathVariable Integer id) {
        DetalleReceta detalle = detalleRecetaService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));
        Receta receta = detalle.getReceta();
        detalleRecetaService.eliminar(id);
        // Si la receta queda sin detalles, eliminarla
        if (recetaService.obtenerPorConsulta(receta.getConsulta().getPkConsulta()).isEmpty()) {
            recetaService.eliminar(receta.getPkReceta());
        }
    }

    // --- Métodos privados de conversión y construcción ---
    private MedicamentoResponse convertirMedicamento(Medicamento medicamento) {
        List<DetalleMedicamento> detalles = detalleMedicamentoService.obtenerPorMedicamento(medicamento.getPkMedicamento());
        DetalleMedicamento detalle = detalles.isEmpty() ? null : detalles.get(0);
        return new MedicamentoResponse(
                medicamento.getPkMedicamento(),
                medicamento.getNombre(),
                detalle != null ? detalle.getPkDetalleMedicamento() : null,
                detalle != null ? detalle.getPresentacion() : null,
                detalle != null ? detalle.getConcentracion() : null,
                detalle != null ? detalle.getEntradas() : null,
                detalle != null ? detalle.getSalidas() : null,
                detalle != null ? detalle.getLotes() : null,
                detalle != null ? detalle.getVencimientos() : null
        );
    }

    private DetalleMedicamento construirDetalle(MedicamentoRequest request) {
        DetalleMedicamento detalle = new DetalleMedicamento();
        detalle.setPresentacion(request.presentacion());
        detalle.setConcentracion(request.concentracion());
        detalle.setEntradas(request.entradas());
        detalle.setSalidas(request.salidas());
        detalle.setLotes(request.lote());
        detalle.setVencimientos(request.vencimiento());
        return detalle;
    }

    private AsignacionResponse convertirAsignacion(DetalleReceta detalle) {
        Consulta consulta = detalle.getReceta().getConsulta();
        return new AsignacionResponse(
                detalle.getPkDetalleReceta(),
                consulta.getPkConsulta(),
                consulta.getExpediente().getPaciente().getPkPaciente(),
                consulta.getExpediente().getPaciente().getNombre(),
                detalle.getMedicamento().getPkMedicamento(),
                detalle.getMedicamento().getNombre(),
                detalle.getDosis(),
                detalle.getFrecuencia()
        );
    }

    // --- DTOs ---
    public record MedicamentoRequest(String nombre, String presentacion, String concentracion,
                                     Integer entradas, Integer salidas, String lote, LocalDate vencimiento) {}

    public record MedicamentoResponse(Integer id, String nombre, Integer detalleId, String presentacion,
                                      String concentracion, Integer entradas, Integer salidas,
                                      String lote, LocalDate vencimiento) {}

    public record AsignacionRequest(Integer pacienteId, Integer consultaId, Integer medicamentoId,
                                    String dosis, String frecuencia) {}

    public record AsignacionResponse(Integer id, Integer consultaId, Integer pacienteId, String paciente,
                                     Integer medicamentoId, String medicamento, String dosis, String frecuencia) {}
}