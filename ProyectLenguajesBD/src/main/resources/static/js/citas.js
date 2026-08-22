document.addEventListener('DOMContentLoaded', () => {
    const tabla = document.querySelector('table tbody');
    const buscarInput = document.getElementById('buscarCita');
    const estadoSelect = document.getElementById('estadoFiltro');
    const buscarBtn = document.getElementById('btnBuscarCita');
    let citaEnEdicion = null;

    // ========== FUNCIÓN: SOLO NÚMEROS EN DURACIÓN ==========
    function soloNumeros(event) {
        const input = event.target;
        const valor = input.value;
        const soloDigitos = valor.replace(/\D/g, '');
        if (valor !== soloDigitos) {
            input.value = soloDigitos;
        }
    }

    const duracionInputs = ['duracion', 'editarDuracion'];
    duracionInputs.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', soloNumeros);
        }
    });

    // ========== CARGAR CITAS ==========
    async function cargarCitas() {
        const respuesta = await fetch('/api/citas');
        const citas = await respuesta.json();
        tabla.innerHTML = citas.map(cita => {
            const fecha = cita.fechaHora.slice(0, 10);
            const hora = cita.fechaHora.slice(11, 16);
            return `<tr data-id="${cita.id}" data-paciente-id="${cita.pacienteId}" data-consultorio-id="${cita.consultorioId}" data-duracion="${cita.duracion || ''}">
                <td>${cita.id}</td>
                <td>${cita.paciente}</td>
                <td>${fecha}</td>
                <td>${hora}</td>
                <td>${cita.consultorio}</td>
                <td>${cita.duracion || ''}</td>
                <td>${cita.estado}</td>
                <td class="text-center">
                    <button class="btn btn-info btn-sm accion-btn" data-action="ver"><i class="bi bi-eye"></i></button>
                    <button class="btn btn-warning btn-sm accion-btn" data-action="editar"><i class="bi bi-pencil"></i></button>
                    <button class="btn btn-danger btn-sm accion-btn" data-action="eliminar"><i class="bi bi-trash"></i></button>
                </td>
            </tr>`;
        }).join('');
        filtrarCitas();
    }

    cargarCitas();

    
    // ========== FILTRO COMBINADO ==========
    function filtrarCitas() {
        const texto = buscarInput.value.toLowerCase();
        const estado = estadoSelect.value;
        const filas = tabla.querySelectorAll('tr');
        filas.forEach(fila => {
            const textoFila = fila.textContent.toLowerCase();
            const estadoFila = fila.cells[6]?.textContent.trim() || ''; // índice 6 = estado
            const coincideTexto = textoFila.includes(texto);
            const coincideEstado = estado === 'todos' || estado === '' || estadoFila === estado;
            fila.style.display = (coincideTexto && coincideEstado) ? '' : 'none';
        });
    }

    buscarInput.addEventListener('keyup', filtrarCitas);
    buscarBtn.addEventListener('click', filtrarCitas);
    estadoSelect.addEventListener('change', filtrarCitas);

    // ========== VALIDAR FECHA/HORA FUTURA ==========
    function esFechaHoraValida(fecha, hora) {
        const ahora = new Date();
        const hoy = new Date(ahora.getFullYear(), ahora.getMonth(), ahora.getDate());
        const fechaSeleccionada = new Date(fecha);
        // Comparar solo fechas (sin hora)
        const esHoy = fechaSeleccionada.getTime() === hoy.getTime();
        const esFuturo = fechaSeleccionada > hoy;

        if (esFuturo) return true; // cualquier hora es válida

        if (esHoy) {
            // Para hoy, la hora debe ser al menos 1 minuto después de la hora actual
            const ahoraMinutos = ahora.getHours() * 60 + ahora.getMinutes();
            const [h, m] = hora.split(':').map(Number);
            const horaMinutos = h * 60 + m;
            return horaMinutos > ahoraMinutos; // estrictamente mayor (min 1 minuto)
        }

        return false; // fecha pasada
    }

    // ========== NUEVA CITA ==========
    document.getElementById('btnNuevaCita').addEventListener('click', () => {
        const ahora = new Date();
        const fechaActual = ahora.toISOString().slice(0, 10);
        const horaActual = ahora.toTimeString().slice(0, 5);

        const fechaInput = document.getElementById('fecha');
        const horaInput = document.getElementById('hora');

        fechaInput.setAttribute('min', fechaActual);

        document.getElementById('paciente').selectedIndex = 0;
        document.getElementById('medico').selectedIndex = 0;
        document.getElementById('consultorio').selectedIndex = 0;
        document.getElementById('estado').value = 'Pendiente';
        document.getElementById('duracion').value = '';
        fechaInput.value = fechaActual;
        horaInput.value = horaActual;
    });

    document.getElementById('guardarCita').addEventListener('click', async () => {
        const fecha = document.getElementById('fecha').value;
        const hora = document.getElementById('hora').value;
        const medicoId = Number(document.getElementById('medico').value);
        const pacienteId = Number(document.getElementById('paciente').value);
        const consultorioId = Number(document.getElementById('consultorio').value);
        const duracion = document.getElementById('duracion').value;
        const estado = document.getElementById('estado').value;

        if (!pacienteId || !consultorioId || !medicoId || !fecha || !hora) {
            alert('Complete todos los campos obligatorios (Paciente, Médico, Consultorio, Fecha y Hora)');
            return;
        }

        if (!esFechaHoraValida(fecha, hora)) {
            alert('La cita debe ser en un momento futuro. Si es hoy, la hora debe ser posterior a la actual.');
            return;
        }

        const datos = {
            pacienteId,
            consultorioId,
            medicoId,
            fechaHora: `${fecha}T${hora}`,
            duracion: duracion || null,
            estado
        };

        await fetch('/api/citas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        bootstrap.Modal.getInstance(document.getElementById('modalNuevaCita')).hide();
        await cargarCitas();
    });

    // ========== EVENTOS EN TABLA ==========
    tabla.addEventListener('click', async event => {
        const boton = event.target.closest('.accion-btn');
        if (!boton) return;
        const fila = boton.closest('tr');

        if (boton.dataset.action === 'ver') {
            document.getElementById('verPaciente').textContent = fila.cells[1].textContent;
            document.getElementById('verFecha').textContent = fila.cells[2].textContent;
            document.getElementById('verHora').textContent = fila.cells[3].textContent;
            document.getElementById('verConsultorio').textContent = fila.cells[4].textContent;
            document.getElementById('verDuracion').textContent = fila.cells[5].textContent || 'N/A';
            document.getElementById('verEstado').textContent = fila.cells[6].textContent;
            // Médico no está en la tabla, pero lo podríamos obtener del backend. Por ahora lo dejamos vacío.
            document.getElementById('verMedico').textContent = 'No disponible'; // Podrías cargarlo con fetch si lo necesitas
            new bootstrap.Modal(document.getElementById('modalVerCita')).show();
        } else if (boton.dataset.action === 'editar') {
            citaEnEdicion = fila;
            document.getElementById('editarPaciente').value = fila.dataset.pacienteId;
            document.getElementById('editarFecha').value = fila.cells[2].textContent;
            document.getElementById('editarHora').value = fila.cells[3].textContent;
            document.getElementById('editarConsultorio').value = fila.dataset.consultorioId;
            document.getElementById('editarDuracion').value = fila.dataset.duracion;
            document.getElementById('editarEstado').value = fila.cells[6].textContent;
            new bootstrap.Modal(document.getElementById('modalEditarCita')).show();
        } else if (boton.dataset.action === 'eliminar' && confirm('¿Eliminar esta cita?')) {
            await fetch(`/api/citas/${fila.dataset.id}`, { method: 'DELETE' });
            await cargarCitas();
        }
    });

    // ========== ACTUALIZAR CITA ==========
    document.getElementById('actualizarCita').addEventListener('click', async () => {
        const fecha = document.getElementById('editarFecha').value;
        const hora = document.getElementById('editarHora').value;
        const pacienteId = Number(document.getElementById('editarPaciente').value);
        const consultorioId = Number(document.getElementById('editarConsultorio').value);
        const duracion = document.getElementById('editarDuracion').value;
        const estado = document.getElementById('editarEstado').value;

        if (!pacienteId || !consultorioId || !fecha || !hora) {
            alert('Complete todos los campos obligatorios');
            return;
        }

        if (!esFechaHoraValida(fecha, hora)) {
            alert('La cita debe ser en un momento futuro. Si es hoy, la hora debe ser posterior a la actual.');
            return;
        }

        const datos = {
            pacienteId,
            consultorioId,
            fechaHora: `${fecha}T${hora}`,
            duracion: duracion || null,
            estado
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