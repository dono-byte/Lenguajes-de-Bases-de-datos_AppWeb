document.addEventListener('DOMContentLoaded', () => {
const buscarPaciente = document.getElementById('buscarPaciente');
const tablaConsultas = document.getElementById('tablaConsultas');
let consultaEnEdicion = null;

cargarConsultas();

async function cargarConsultas() {
    const respuesta = await fetch('/api/consultas');
    const consultas = await respuesta.json();
    tablaConsultas.innerHTML = consultas.map(consulta => `<tr data-id="${consulta.id}" data-paciente-id="${consulta.pacienteId}" data-cita-id="${consulta.citaId}" data-medico-id="${consulta.medicoId}" data-diagnostico-id="${consulta.diagnosticoId || ''}" data-observaciones="${consulta.observaciones || ''}">
        <td>${consulta.fechaConsulta}</td><td>${consulta.paciente}</td><td>${consulta.medico}</td>
        <td>${consulta.motivo || ''}</td><td>${consulta.diagnostico || ''}</td>
        <td class="text-center"><button class="btn btn-info btn-sm accion-btn" data-action="ver"><i class="bi bi-eye"></i></button>
        <button class="btn btn-warning btn-sm accion-btn" data-action="editar"><i class="bi bi-pencil-square"></i></button>
        <button class="btn btn-danger btn-sm accion-btn" data-action="eliminar"><i class="bi bi-trash"></i></button></td>
    </tr>`).join('');
}

// BUSCADOR GENERAL DE CONSULTAS
buscarPaciente.addEventListener('keyup', function () {
    const texto = this.value.toLowerCase();
    tablaConsultas.querySelectorAll('tr').forEach(fila => fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none');
});

document.getElementById('guardarConsulta').addEventListener('click', async () => {
    const datos = {fechaConsulta: document.getElementById('fechaConsulta').value,
        pacienteId: Number(document.getElementById('pacienteConsulta').value),
        citaId: Number(document.getElementById('citaConsulta').value),
        medicoId: Number(document.getElementById('doctorConsulta').value),
        diagnosticoId: document.getElementById('diagnosticoConsulta').value ? Number(document.getElementById('diagnosticoConsulta').value) : null,
        motivo: document.getElementById('motivoConsulta').value,
        observaciones: document.getElementById('observacionesConsulta').value};
    if (!datos.fechaConsulta || !datos.pacienteId || !datos.citaId || !datos.medicoId || !datos.motivo) {
        alert('Complete los campos obligatorios');
        return;
    }
    await fetch('/api/consultas', {method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(datos)});
    bootstrap.Modal.getInstance(document.getElementById('modalNuevaConsulta')).hide();
    await cargarConsultas();
});

tablaConsultas.addEventListener('click', async event => {
    let boton = event.target.closest('.accion-btn');
    if (!boton) return;
    const fila = boton.closest('tr');
    if (boton.dataset.action === 'ver') {
        document.getElementById('verFecha').textContent = fila.cells[0].textContent;
        document.getElementById('verPaciente').textContent = fila.cells[1].textContent;
        document.getElementById('verDoctor').textContent = fila.cells[2].textContent;
        document.getElementById('verMotivo').textContent = fila.cells[3].textContent;
        document.getElementById('verDiagnostico').textContent = fila.cells[4].textContent;
        new bootstrap.Modal(document.getElementById('modalVerConsulta')).show();
    } else if (boton.dataset.action === 'editar') {
        consultaEnEdicion = fila;
        document.getElementById('editarPaciente').value = fila.dataset.pacienteId;
        document.getElementById('editarCita').value = fila.dataset.citaId;
        document.getElementById('editarDoctor').value = fila.dataset.medicoId;
        document.getElementById('editarMotivo').value = fila.cells[3].textContent;
        document.getElementById('editarDiagnostico').value = fila.dataset.diagnosticoId;
        document.getElementById('editarObservaciones').value = fila.dataset.observaciones;
        new bootstrap.Modal(document.getElementById('modalEditarConsulta')).show();
    } else if (boton.dataset.action === 'eliminar' && confirm('¿Eliminar esta consulta?')) {
        await fetch(`/api/consultas/${fila.dataset.id}`, {method: 'DELETE'});
        await cargarConsultas();
    }
});

document.getElementById('actualizarConsulta').addEventListener('click', async () => {
    const datos = {pacienteId: Number(document.getElementById('editarPaciente').value),
        citaId: Number(document.getElementById('editarCita').value),
        medicoId: Number(document.getElementById('editarDoctor').value),
        diagnosticoId: document.getElementById('editarDiagnostico').value ? Number(document.getElementById('editarDiagnostico').value) : null,
        fechaConsulta: consultaEnEdicion.cells[0].textContent,
        motivo: document.getElementById('editarMotivo').value, observaciones: document.getElementById('editarObservaciones').value};
    await fetch(`/api/consultas/${consultaEnEdicion.dataset.id}`, {method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(datos)});
    bootstrap.Modal.getInstance(document.getElementById('modalEditarConsulta')).hide();
    await cargarConsultas();
});
});

function filtrarCitas(selectPaciente, selectCita) {
    const pacienteId = selectPaciente.value;
    Array.from(selectCita.options).forEach(opcion => {
        opcion.hidden = opcion.value !== '' && opcion.dataset.pacienteId !== pacienteId;
    });
    if (selectCita.selectedOptions[0]?.hidden) selectCita.value = '';
}

document.getElementById('pacienteConsulta').addEventListener('change', () =>
    filtrarCitas(document.getElementById('pacienteConsulta'), document.getElementById('citaConsulta')));

document.getElementById('editarPaciente').addEventListener('change', () =>
    filtrarCitas(document.getElementById('editarPaciente'), document.getElementById('editarCita')));