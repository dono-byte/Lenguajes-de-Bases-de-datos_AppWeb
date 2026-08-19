document.addEventListener('DOMContentLoaded', () => {
const buscarPaciente = document.getElementById('buscarPaciente');
const tablaPacientes = document.querySelector('table tbody');
let pacienteEnEdicion = null;

cargarPacientes();

async function cargarPacientes() {
    const respuesta = await fetch('/api/pacientes');
    const pacientes = await respuesta.json();
    tablaPacientes.innerHTML = pacientes.map(paciente => `
        <tr data-id="${paciente.id}">
            <td>${paciente.id}</td><td>${paciente.cedula}</td><td>${paciente.nombre}</td>
            <td>${paciente.fechaNacimiento}</td><td>${paciente.genero}</td>
            <td>${paciente.telefono || ''}</td><td>${paciente.direccion || ''}</td>
            <td class="text-center">
                <button class="btn btn-info btn-sm accion-btn" data-action="ver"><i class="bi bi-eye"></i></button>
                <button class="btn btn-warning btn-sm accion-btn" data-action="editar"><i class="bi bi-pencil-square"></i></button>
                <button class="btn btn-danger btn-sm accion-btn" data-action="eliminar"><i class="bi bi-trash"></i></button>
            </td>
        </tr>`).join('');
}

// BUSCADOR GENERAL DE PACIENTES
buscarPaciente.addEventListener('keyup', function () {
    const texto = this.value.toLowerCase();
    tablaPacientes.querySelectorAll('tr').forEach(fila => {
        fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
    });
});

document.getElementById('btnNuevoPaciente').addEventListener('click', () => {
    pacienteEnEdicion = null;
    document.getElementById('formPaciente').reset();
});

document.getElementById('guardarPaciente').addEventListener('click', async () => {
    const datos = {
        cedula: pacienteEnEdicion ? pacienteEnEdicion.cells[1].textContent : document.getElementById('cedula').value,
        nombre: document.getElementById(pacienteEnEdicion ? 'editarNombre' : 'nombre').value,
        fechaNacimiento: document.getElementById(pacienteEnEdicion ? 'editarFechaNacimiento' : 'fechaNacimiento').value,
        genero: document.getElementById(pacienteEnEdicion ? 'editarGenero' : 'genero').value,
        telefono: document.getElementById(pacienteEnEdicion ? 'editarTelefono' : 'telefono').value,
        direccion: document.getElementById(pacienteEnEdicion ? 'editarDireccion' : 'direccion').value
    };
    if (!datos.cedula || !datos.nombre || !datos.fechaNacimiento || !datos.genero) {
        alert('Complete los campos obligatorios');
        return;
    }
    await fetch(pacienteEnEdicion ? `/api/pacientes/${pacienteEnEdicion.dataset.id}` : '/api/pacientes', {
        method: pacienteEnEdicion ? 'PUT' : 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(datos)
    });
    bootstrap.Modal.getInstance(document.getElementById('modalNuevoPaciente')).hide();
    await cargarPacientes();
});

tablaPacientes.addEventListener('click', async event => {
    const boton = event.target.closest('.accion-btn');
    if (!boton) return;
    const fila = boton.closest('tr');
    if (boton.dataset.action === 'ver') {
        document.getElementById('verCedula').textContent = fila.cells[1].textContent;
        document.getElementById('verNombre').textContent = fila.cells[2].textContent;
        document.getElementById('verFechaNacimiento').textContent = fila.cells[3].textContent;
        document.getElementById('verGenero').textContent = fila.cells[4].textContent;
        document.getElementById('verTelefono').textContent = fila.cells[5].textContent;
        document.getElementById('verDireccion').textContent = fila.cells[6].textContent;
        new bootstrap.Modal(document.getElementById('modalVerPaciente')).show();
    } else if (boton.dataset.action === 'editar') {
        pacienteEnEdicion = fila;
        document.getElementById('editarNombre').value = fila.cells[2].textContent;
        document.getElementById('editarFechaNacimiento').value = fila.cells[3].textContent;
        document.getElementById('editarGenero').value = fila.cells[4].textContent;
        document.getElementById('editarTelefono').value = fila.cells[5].textContent;
        document.getElementById('editarDireccion').value = fila.cells[6].textContent;
        new bootstrap.Modal(document.getElementById('modalEditarPaciente')).show();
    } else if (boton.dataset.action === 'eliminar' && confirm('¿Eliminar este paciente?')) {
        await fetch(`/api/pacientes/${fila.dataset.id}`, {method: 'DELETE'});
        await cargarPacientes();
    }
});

document.getElementById('actualizarPaciente').addEventListener('click', async () => {
    const datos = {
        cedula: pacienteEnEdicion.cells[1].textContent,
        nombre: document.getElementById('editarNombre').value,
        fechaNacimiento: document.getElementById('editarFechaNacimiento').value,
        genero: document.getElementById('editarGenero').value,
        telefono: document.getElementById('editarTelefono').value,
        direccion: document.getElementById('editarDireccion').value
    };
    await fetch(`/api/pacientes/${pacienteEnEdicion.dataset.id}`, {method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(datos)});
    bootstrap.Modal.getInstance(document.getElementById('modalEditarPaciente')).hide();
    await cargarPacientes();
});
});