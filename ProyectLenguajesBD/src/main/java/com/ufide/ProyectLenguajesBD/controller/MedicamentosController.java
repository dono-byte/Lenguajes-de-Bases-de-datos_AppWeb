package com.ufide.ProyectLenguajesBD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ufide.ProyectLenguajesBD.entity.DetalleMedicamento;
import com.ufide.ProyectLenguajesBD.entity.Medicamento;
import com.ufide.ProyectLenguajesBD.repository.DetalleMedicamentoRepository;
import com.ufide.ProyectLenguajesBD.repository.MedicamentoRepository;
import com.ufide.ProyectLenguajesBD.repository.DetalleRecetaRepository;
import com.ufide.ProyectLenguajesBD.repository.RecetaRepository;
import com.ufide.ProyectLenguajesBD.repository.ConsultaRepository;
import com.ufide.ProyectLenguajesBD.repository.PacienteRepository;
import com.ufide.ProyectLenguajesBD.entity.Consulta;
import com.ufide.ProyectLenguajesBD.entity.DetalleReceta;
import com.ufide.ProyectLenguajesBD.entity.Receta;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.ufide.ProyectLenguajesBD.service.MedicamentoService;

@Controller
public class MedicamentosController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private DetalleMedicamentoRepository detalleMedicamentoRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private DetalleRecetaRepository detalleRecetaRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping({"/medicamentos", "/medicamentos.html"})
    public String verMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoService.obtenerTodos());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("consultas", consultaRepository.findAll());
        return "medicamentos";
    }

    @GetMapping("/api/medicamentos")
    @ResponseBody
    public List<MedicamentoResponse> listar() {
        return medicamentoService.obtenerTodos().stream().map(this::convertir).collect(Collectors.toList());
    }

    @PostMapping("/api/medicamentos")
    @ResponseBody
    public MedicamentoResponse crear(@RequestBody MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento(request.nombre());
        DetalleMedicamento detalle = construirDetalle(request);
        medicamentoService.guardarConDetalle(medicamento, detalle);
        return convertir(medicamento);
    }

    @PutMapping("/api/medicamentos/{id}")
    @ResponseBody
    public MedicamentoResponse actualizar(@PathVariable Integer id, @RequestBody MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento(request.nombre());
        DetalleMedicamento detalle = construirDetalle(request);
        return medicamentoService.actualizarConDetalle(id, medicamento, detalle)
                .map(this::convertir)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
    }

    @DeleteMapping("/api/medicamentos/{id}")
    @ResponseBody
    public void eliminar(@PathVariable Integer id) {
        medicamentoService.eliminar(id);
    }

    private MedicamentoResponse convertir(Medicamento medicamento) {
        DetalleMedicamento detalle = detalleMedicamentoRepository
                .findByMedicamentoPkMedicamento(medicamento.getPkMedicamento()).stream().findFirst().orElse(null);
        return new MedicamentoResponse(
                medicamento.getPkMedicamento(), medicamento.getNombre(),
                detalle == null ? null : detalle.getPkDetalleMedicamento(),
                detalle == null ? null : detalle.getPresentacion(),
                detalle == null ? null : detalle.getConcentracion(),
                detalle == null ? null : detalle.getEntradas(),
                detalle == null ? null : detalle.getSalidas(),
                detalle == null ? null : detalle.getLotes(),
                detalle == null ? null : detalle.getVencimientos());
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

    public record MedicamentoRequest(String nombre, String presentacion, String concentracion,
            Integer entradas, Integer salidas, String lote, LocalDate vencimiento) {}

    public record MedicamentoResponse(Integer id, String nombre, Integer detalleId, String presentacion,
            String concentracion, Integer entradas, Integer salidas, String lote, LocalDate vencimiento) {}

        @GetMapping("/api/asignaciones-medicamentos")
        @ResponseBody
        public List<AsignacionResponse> listarAsignaciones() {
        return detalleRecetaRepository.findAll().stream().map(this::convertirAsignacion).toList();
        }

        @PostMapping("/api/asignaciones-medicamentos")
        @ResponseBody
        @Transactional
        public AsignacionResponse asignarMedicamento(@RequestBody AsignacionRequest request) {
        Consulta consulta = consultaRepository.findById(request.consultaId()).orElseThrow();
        if (!consulta.getExpediente().getPaciente().getPkPaciente().equals(request.pacienteId())) {
            throw new IllegalArgumentException("La consulta no pertenece al paciente seleccionado");
        }
        Receta receta = recetaRepository.findByConsultaPkConsulta(consulta.getPkConsulta()).stream().findFirst()
            .orElseGet(() -> recetaRepository.save(new Receta(consulta, LocalDate.now())));
        DetalleReceta detalle = new DetalleReceta(receta,
            medicamentoRepository.findById(request.medicamentoId()).orElseThrow(), request.dosis(), request.frecuencia());
        return convertirAsignacion(detalleRecetaRepository.save(detalle));
        }

    @PutMapping("/api/asignaciones-medicamentos/{id}")
    @ResponseBody
    @Transactional
    public AsignacionResponse actualizarAsignacion(@PathVariable Integer id,
            @RequestBody AsignacionRequest request) {
        DetalleReceta detalle = detalleRecetaRepository.findById(id).orElseThrow();
        Consulta consulta = consultaRepository.findById(request.consultaId()).orElseThrow();
        if (!consulta.getExpediente().getPaciente().getPkPaciente().equals(request.pacienteId())) {
            throw new IllegalArgumentException("La consulta no pertenece al paciente seleccionado");
        }
        detalle.setReceta(recetaRepository.findByConsultaPkConsulta(consulta.getPkConsulta()).stream().findFirst()
            .orElseGet(() -> recetaRepository.save(new Receta(consulta, LocalDate.now()))));
        detalle.setMedicamento(medicamentoRepository.findById(request.medicamentoId()).orElseThrow());
        detalle.setDosis(request.dosis());
        detalle.setFrecuencia(request.frecuencia());
        return convertirAsignacion(detalleRecetaRepository.save(detalle));
    }

    @DeleteMapping("/api/asignaciones-medicamentos/{id}")
    @ResponseBody
    @Transactional
    public void eliminarAsignacion(@PathVariable Integer id) {
        DetalleReceta detalle = detalleRecetaRepository.findById(id).orElseThrow();
        Receta receta = detalle.getReceta();
        detalleRecetaRepository.delete(detalle);
        if (detalleRecetaRepository.findByRecetaPkReceta(receta.getPkReceta()).isEmpty()) {
            recetaRepository.delete(receta);
        }
    }

        private AsignacionResponse convertirAsignacion(DetalleReceta detalle) {
        Consulta consulta = detalle.getReceta().getConsulta();
        return new AsignacionResponse(detalle.getPkDetalleReceta(), consulta.getPkConsulta(),
            consulta.getExpediente().getPaciente().getPkPaciente(), consulta.getExpediente().getPaciente().getNombre(),
            detalle.getMedicamento().getPkMedicamento(), detalle.getMedicamento().getNombre(),
            detalle.getDosis(), detalle.getFrecuencia());
        }

        public record AsignacionRequest(Integer pacienteId, Integer consultaId, Integer medicamentoId,
            String dosis, String frecuencia) {}

        public record AsignacionResponse(Integer id, Integer consultaId, Integer pacienteId, String paciente,
            Integer medicamentoId, String medicamento, String dosis, String frecuencia) {}
}
