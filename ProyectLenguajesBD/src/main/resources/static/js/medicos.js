document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.querySelector('table tbody');
    const btnGuardar = document.getElementById('guardarMedico');
    const btnAbrirModalNuevo = document.getElementById('btnAbrirModalNuevoMedico');
    const buscarMedico = document.getElementById('buscarMedico');

    // URL base (sin contexto, pues tu app está en raíz)
    const API_BASE = '/api/medicos';

    let filaEnEdicion = null;

    // Cargar médicos desde la API
    async function cargarMedicos() {
        try {
            const respuesta = await fetch(API_BASE);
            if (!respuesta.ok) {
                throw new Error(`HTTP ${respuesta.status} - ${respuesta.statusText}`);
            }
            const medicos = await respuesta.json();
            if (!Array.isArray(medicos)) {
                throw new Error('La respuesta no es un array');
            }

            tabla.innerHTML = medicos.map(medico => `
                <tr data-id="${medico.id}" 
                    data-usuario-id="${medico.usuarioId}" 
                    data-codigo="${medico.codigoMedico}" 
                    data-especialidad-id="${medico.especialidadId}"
                    data-nombre="${medico.nombre}" 
                    data-apellido="${medico.apellido}">
                    <td>${medico.id}</td>
                    <td>${medico.nombre} ${medico.apellido}</td>
                    <td>${medico.especialidad || ''}</td>
                    <td>${medico.telefono || ''}</td>
                    <td>${medico.correoElectronico}</td>
                    <td>${medico.estado}</td>
                    <td class="text-center">
                        <button class="btn btn-info btn-sm btn-ver"><i class="bi bi-eye"></i></button>
                        <button class="btn btn-warning btn-sm btn-editar"><i class="bi bi-pencil"></i></button>
                        <button class="btn btn-danger btn-sm btn-eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                </tr>
            `).join('');

            console.log('Médicos cargados correctamente');
        } catch (error) {
            console.error('Error al cargar médicos:', error);
            tabla.innerHTML = `<tr><td colspan="7" class="text-center text-danger">Error al cargar los médicos. Ver consola.</td></tr>`;
        }
    }

    // Cargar al inicio
    cargarMedicos();

    // Buscador
    if (buscarMedico) {
        buscarMedico.addEventListener('keyup', function () {
            const texto = this.value.toLowerCase();
            const filas = tabla.querySelectorAll('tr');
            filas.forEach(fila => {
                const datos = fila.textContent.toLowerCase();
                fila.style.display = datos.includes(texto) ? '' : 'none';
            });
        });
    }

    // Botón "Nuevo médico"
    if (btnAbrirModalNuevo) {
        btnAbrirModalNuevo.addEventListener('click', function () {
            filaEnEdicion = null;
            document.getElementById('tituloModalMedico').innerText = 'Nuevo médico';
            document.getElementById('usuarioMedico').value = '';
            document.getElementById('codigoMedico').value = '';
            document.getElementById('apellidoMedico').value = '';
            document.getElementById('nombreMedico').value = '';
            document.getElementById('especialidadMedico').value = '';
            document.getElementById('telefonoMedico').value = '';
            document.getElementById('correoMedico').value = '';
            document.getElementById('estadoMedico').value = 'Activo';
        });
    }

    // Guardar (Crear o Actualizar)
    if (btnGuardar) {
        btnGuardar.addEventListener('click', async function () {
            const usuarioId = document.getElementById('usuarioMedico').value;
            const codigoMedico = document.getElementById('codigoMedico').value;
            const apellido = document.getElementById('apellidoMedico').value;
            const nombre = document.getElementById('nombreMedico').value;
            const especialidadId = document.getElementById('especialidadMedico').value;
            const telefono = document.getElementById('telefonoMedico').value;
            const correo = document.getElementById('correoMedico').value;
            const estado = document.getElementById('estadoMedico').value;

            // Validar campos obligatorios
            if (!usuarioId || !codigoMedico || !apellido || !nombre || !especialidadId || !correo) {
                alert('Complete los campos obligatorios.');
                return;
            }

            const datos = {
                usuarioId: Number(usuarioId),
                especialidadId: Number(especialidadId),
                nombre,
                apellido,
                segApellido: '', // No tenemos campo en el formulario
                codigoMedico,
                correoElectronico: correo,
                telefono,
                estado
            };

            const url = filaEnEdicion ? `${API_BASE}/${filaEnEdicion.dataset.id}` : API_BASE;
            const method = filaEnEdicion ? 'PUT' : 'POST';

            try {
                const respuesta = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(datos)
                });

                if (!respuesta.ok) {
                    const errorText = await respuesta.text();
                    throw new Error(`Error ${respuesta.status}: ${errorText}`);
                }

                // Recargar tabla
                await cargarMedicos();

                // Cerrar modal
                const modalEl = document.getElementById('modalMedico');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if (modal) modal.hide();

                console.log('Médico guardado correctamente');
            } catch (error) {
                console.error('Error al guardar:', error);
                alert('Error al guardar el médico. Ver consola.');
            }
        });
    }

    // Eventos de los botones en la tabla (Ver, Editar, Eliminar)
    if (tabla) {
        tabla.addEventListener('click', async function (e) {
            const fila = e.target.closest('tr');
            if (!fila) return;

            // Botón Eliminar
            if (e.target.closest('.btn-eliminar')) {
                if (!confirm('¿Está seguro de que desea eliminar este registro?')) return;
                const id = fila.dataset.id;
                if (!id) {
                    alert('ID no encontrado');
                    return;
                }
                try {
                    const respuesta = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
                    if (!respuesta.ok) {
                        throw new Error(`HTTP ${respuesta.status}`);
                    }
                    await cargarMedicos();
                    console.log('Médico eliminado');
                } catch (error) {
                    console.error('Error al eliminar:', error);
                    alert('Error al eliminar. Ver consola.');
                }
                return;
            }

            // Botón Ver
            if (e.target.closest('.btn-ver')) {
                document.getElementById('verNombreMedico').innerText = fila.cells[1].innerText;
                document.getElementById('verEspecialidadMedico').innerText = fila.cells[2].innerText;
                document.getElementById('verTelefonoMedico').innerText = fila.cells[3].innerText;
                document.getElementById('verCorreoMedico').innerText = fila.cells[4].innerText;
                document.getElementById('verEstadoMedico').innerText = fila.cells[5].innerText.trim();

                const modalVer = new bootstrap.Modal(document.getElementById('modalVerMedico'));
                modalVer.show();
                return;
            }

            // Botón Editar
            if (e.target.closest('.btn-editar')) {
                filaEnEdicion = fila;

                document.getElementById('tituloModalMedico').innerText = 'Editar médico';
                document.getElementById('usuarioMedico').value = fila.dataset.usuarioId || '';
                document.getElementById('codigoMedico').value = fila.dataset.codigo || '';
                document.getElementById('nombreMedico').value = fila.dataset.nombre || '';
                document.getElementById('apellidoMedico').value = fila.dataset.apellido || '';
                document.getElementById('especialidadMedico').value = fila.dataset.especialidadId || '';
                document.getElementById('telefonoMedico').value = fila.cells[3].innerText || '';
                document.getElementById('correoMedico').value = fila.cells[4].innerText || '';
                document.getElementById('estadoMedico').value = fila.cells[5].innerText.trim() || 'Activo';

                const modalEdicion = new bootstrap.Modal(document.getElementById('modalMedico'));
                modalEdicion.show();
            }
        });
    } else {
        console.error('No se encontró el elemento <tbody> de la tabla');
    }
});