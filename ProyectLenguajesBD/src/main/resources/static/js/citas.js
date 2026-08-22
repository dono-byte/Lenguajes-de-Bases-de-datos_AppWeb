document.addEventListener('DOMContentLoaded', () => {
    const tabla = document.querySelector('table tbody');
    const buscarInput = document.getElementById('buscarCita');
    const estadoSelect = document.getElementById('filtrarEstado');
    const btnBuscar = document.getElementById('btnBuscarCita');
    let citaEnEdicion = null;

    cargarCitas();

    async function cargarCitas() {
        const respuesta = await fetch('/api/citas');
        const citas = await respuesta.json();
        tabla.innerHTML = citas.map(cita => {
            const fecha = cita.fechaHora.slice(0, 10);
            const hora = cita.fechaHora.slice(11, 16);
            return `<tr data-id="${cita.id}" data-paciente-id="${cita.pacienteId}" data-consultorio-id="${cita.consultorioId}" data-duracion="${cita.duracion || ''}">
                <td>${cita.id}</td><td>${cita.paciente}</td><td>${fecha}</td><td>${hora}</td>
                <td>${cita.consultorio}</td><td>${cita.localidad || ''}</td><td>${cita.provincia || ''}</td><td>${cita.estado}</td>
                <td class="text-center"><button class="btn btn-info btn-sm accion-btn" data-action="ver"><i class="bi bi-eye"></i></button>
                <button class="btn btn-warning btn-sm accion-btn" data-action="editar"><i class="bi bi-pencil"></i></button>
                <button class="btn btn-danger btn-sm accion-btn" data-action="eliminar"><i class="bi bi-trash"></i></button></td>
            </tr>`;
        }).join('');

        // Aplicar filtros después de cargar (por si hay selección previa)
        aplicarFiltros();
    }

    // ========== FILTRO COMBINADO ==========
    function aplicarFiltros() {
        const texto = buscarInput.value.toLowerCase().trim();
        const estado = estadoSelect.value;

        const filas = tabla.querySelectorAll('tr');
        filas.forEach(fila => {
            const textoFila = fila.textContent.toLowerCase();
            const estadoFila = fila.cells[7]?.textContent.trim() || '';

            let coincideTexto = texto === '' || textoFila.includes(texto);
            let coincideEstado = estado === '' || estado === 'Todos' || estadoFila === estado;

            fila.style.display = (coincideTexto && coincideEstado) ? '' : 'none';
        });
    }

    // Eventos para filtrar
    buscarInput.addEventListener('keyup', aplicarFiltros);
    estadoSelect.addEventListener('change', aplicarFiltros);
    btnBuscar.addEventListener('click', aplicarFiltros);

    // ========== GUARDAR NUEVA CITA ==========
    document.getElementById('guardarCita').addEventListener('click', async () => {
        const fecha = document.getElementById('fecha').value;
        const hora = document.getElementById('hora').value;
        const medicoId = Number(document.getElementById('medico').value);
        const datos = {
            pacienteId: Number(document.getElementById('paciente').value),
            consultorioId: Number(document.getElementById('consultorio').value),
            medicoId,
            fechaHora: `${fecha}T${hora}`,
            duracion: document.getElementById('duracion').value,
            estado: document.getElementById('estado').value
        };
        if (!datos.pacienteId || !datos.consultorioId || !medicoId || !fecha || !hora) {
            alert('Complete los campos obligatorios, incluyendo el médico');
            return;
        }
        await fetch('/api/citas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });
        bootstrap.Modal.getInstance(document.getElementById('modalNuevaCita')).hide();
        await cargarCitas();
    });

    // ========== EVENTOS EN TABLA (VER, EDITAR, ELIMINAR) ==========
    tabla.addEventListener('click', async event => {
        const boton = event.target.closest('.accion-btn');
        if (!boton) return;
        const fila = boton.closest('tr');

        if (boton.dataset.action === 'ver') {
            document.getElementById('verPaciente').textContent = fila.cells[1].textContent;
            document.getElementById('verFecha').textContent = fila.cells[2].textContent;
            document.getElementById('verHora').textContent = fila.cells[3].textContent;
            document.getElementById('verConsultorio').textContent = fila.cells[4].textContent;
            document.getElementById('verCiudad').textContent = fila.cells[5].textContent;
            document.getElementById('verProvincia').textContent = fila.cells[6].textContent;
            document.getElementById('verEstado').textContent = fila.cells[7].textContent;
            new bootstrap.Modal(document.getElementById('modalVerCita')).show();
        } else if (boton.dataset.action === 'editar') {
            citaEnEdicion = fila;
            document.getElementById('editarPaciente').value = fila.dataset.pacienteId;
            document.getElementById('editarFecha').value = fila.cells[2].textContent;
            document.getElementById('editarHora').value = fila.cells[3].textContent;
            document.getElementById('editarConsultorio').value = fila.dataset.consultorioId;
            document.getElementById('editarDuracion').value = fila.dataset.duracion;
            document.getElementById('editarEstado').value = fila.cells[7].textContent;
            new bootstrap.Modal(document.getElementById('modalEditarCita')).show();
        } else if (boton.dataset.action === 'eliminar') {
            if (confirm('¿Eliminar esta cita?')) {
                await fetch(`/api/citas/${fila.dataset.id}`, { method: 'DELETE' });
                await cargarCitas();
            }
        }
    });

    // ========== ACTUALIZAR CITA ==========
    document.getElementById('actualizarCita').addEventListener('click', async () => {
        const datos = {
            pacienteId: Number(document.getElementById('editarPaciente').value),
            consultorioId: Number(document.getElementById('editarConsultorio').value),
            fechaHora: `${document.getElementById('editarFecha').value}T${document.getElementById('editarHora').value}`,
            duracion: document.getElementById('editarDuracion').value,
            estado: document.getElementById('editarEstado').value
        };
        await fetch(`/api/citas/${citaEnEdicion.dataset.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });
        bootstrap.Modal.getInstance(document.getElementById('modalEditarCita')).hide();
        await cargarCitas();
    });
});