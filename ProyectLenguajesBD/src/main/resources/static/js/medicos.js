// public/js/medicos.js

document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.querySelector('table tbody');
    const btnGuardar = document.getElementById('guardarMedico');
    const btnAbrirModalNuevo = document.getElementById('btnAbrirModalNuevoMedico');
    const buscarMedico = document.getElementById('buscarMedico');

    let filaEnEdicion = null;

    cargarMedicos();

    async function cargarMedicos() {
        const respuesta = await fetch('/api/medicos');
        const medicos = await respuesta.json();
        tabla.innerHTML = medicos.map(medico => `<tr data-id="${medico.id}" data-usuario-id="${medico.usuarioId}" data-codigo="${medico.codigoMedico}" data-especialidad-id="${medico.especialidadId}">
            <td>${medico.id}</td><td>${medico.nombre} ${medico.apellido}</td><td>${medico.especialidad || ''}</td>
            <td>${medico.telefono || ''}</td><td>${medico.correoElectronico}</td><td>${medico.estado}</td>
            <td class="text-center"><button class="btn btn-info btn-sm btn-ver"><i class="bi bi-eye"></i></button>
            <button class="btn btn-warning btn-sm btn-editar"><i class="bi bi-pencil"></i></button>
            <button class="btn btn-danger btn-sm btn-eliminar"><i class="bi bi-trash"></i></button></td>
        </tr>`).join('');
    }

    // BUSCADOR GENERAL DE MÉDICOS
    if (buscarMedico) {
        buscarMedico.addEventListener('keyup', function () {
            let texto = this.value.toLowerCase();
            let filas = tabla.querySelectorAll('tr');
            filas.forEach(function (fila) {
                let datos = fila.textContent.toLowerCase();
                fila.style.display = datos.includes(texto) ? '' : 'none';
            });
        });
    }

    function claseEstado(estado) {
        return estado === 'Activo' ? 'bg-success' : 'bg-secondary';
    }

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

            if (!usuarioId || !codigoMedico || !apellido || !nombre || !especialidadId || !correo) {
                alert('Complete los campos obligatorios.');
                return;
            }
            const datos = {usuarioId: Number(usuarioId), especialidadId: Number(especialidadId), nombre, apellido,
                segApellido: '', codigoMedico, correoElectronico: correo, telefono, estado};
            await fetch(filaEnEdicion ? `/api/medicos/${filaEnEdicion.dataset.id}` : '/api/medicos', {
                method: filaEnEdicion ? 'PUT' : 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(datos)
            });
            await cargarMedicos();

            const modalEl = document.getElementById('modalMedico');
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) modal.hide();
        });
    }

    tabla.addEventListener('click', async function (e) {
        const fila = e.target.closest('tr');
        if (!fila) return;

        if (e.target.closest('.btn-eliminar')) {
            if (confirm('¿Está seguro de que desea eliminar este registro?')) {
                await fetch(`/api/medicos/${fila.dataset.id}`, {method: 'DELETE'});
                await cargarMedicos();
            }
        }

        if (e.target.closest('.btn-ver')) {
            document.getElementById('verNombreMedico').innerText = fila.cells[1].innerText;
            document.getElementById('verEspecialidadMedico').innerText = fila.cells[2].innerText;
            document.getElementById('verTelefonoMedico').innerText = fila.cells[3].innerText;
            document.getElementById('verCorreoMedico').innerText = fila.cells[4].innerText;
            document.getElementById('verEstadoMedico').innerText = fila.cells[5].innerText.trim();

            const modalVer = new bootstrap.Modal(document.getElementById('modalVerMedico'));
            modalVer.show();
        }

        if (e.target.closest('.btn-editar')) {
            filaEnEdicion = fila;

            document.getElementById('tituloModalMedico').innerText = 'Editar médico';
            document.getElementById('usuarioMedico').value = fila.dataset.usuarioId;
            document.getElementById('codigoMedico').value = fila.dataset.codigo;
            document.getElementById('nombreMedico').value = fila.cells[1].innerText.split(' ')[0];
            document.getElementById('apellidoMedico').value = fila.cells[1].innerText.split(' ').slice(1).join(' ');
            document.getElementById('especialidadMedico').value = fila.dataset.especialidadId;
            document.getElementById('telefonoMedico').value = fila.cells[3].innerText;
            document.getElementById('correoMedico').value = fila.cells[4].innerText;
            document.getElementById('estadoMedico').value = fila.cells[5].innerText.trim();

            const modalEdicion = new bootstrap.Modal(document.getElementById('modalMedico'));
            modalEdicion.show();
        }
    });
});