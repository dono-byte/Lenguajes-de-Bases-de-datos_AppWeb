// public/js/medicos.js

document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.querySelector('table tbody');
    const btnGuardar = document.getElementById('guardarMedico');
    const btnAbrirModalNuevo = document.getElementById('btnAbrirModalNuevoMedico');
    const buscarMedico = document.getElementById('buscarMedico');

    let filaEnEdicion = null;

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
            document.getElementById('nombreMedico').value = '';
            document.getElementById('especialidadMedico').value = '';
            document.getElementById('telefonoMedico').value = '';
            document.getElementById('correoMedico').value = '';
            document.getElementById('estadoMedico').value = 'Activo';
        });
    }

    if (btnGuardar) {
        btnGuardar.addEventListener('click', function () {
            const nombre = document.getElementById('nombreMedico').value;
            const especialidad = document.getElementById('especialidadMedico').value;
            const telefono = document.getElementById('telefonoMedico').value;
            const correo = document.getElementById('correoMedico').value;
            const estado = document.getElementById('estadoMedico').value;

            if (!nombre || !especialidad) {
                alert('Por favor complete al menos el nombre y la especialidad.');
                return;
            }

            const badgeHtml = `<span class="badge ${claseEstado(estado)}">${estado}</span>`;

            if (filaEnEdicion) {
                filaEnEdicion.cells[1].innerText = nombre;
                filaEnEdicion.cells[2].innerText = especialidad;
                filaEnEdicion.cells[3].innerText = telefono || 'N/A';
                filaEnEdicion.cells[4].innerText = correo || 'N/A';
                filaEnEdicion.cells[5].innerHTML = badgeHtml;
            } else {
                const totalFilas = tabla.rows.length + 1;
                const nuevaFila = document.createElement('tr');
                nuevaFila.innerHTML = `
                    <td>${totalFilas}</td>
                    <td>${nombre}</td>
                    <td>${especialidad}</td>
                    <td>${telefono || 'N/A'}</td>
                    <td>${correo || 'N/A'}</td>
                    <td>${badgeHtml}</td>
                    <td class="text-center">
                        <button class="btn btn-info btn-sm btn-ver"><i class="bi bi-eye"></i></button>
                        <button class="btn btn-warning btn-sm btn-editar"><i class="bi bi-pencil"></i></button>
                        <button class="btn btn-danger btn-sm btn-eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                `;
                tabla.appendChild(nuevaFila);
            }

            const modalEl = document.getElementById('modalMedico');
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) modal.hide();
        });
    }

    tabla.addEventListener('click', function (e) {
        const fila = e.target.closest('tr');
        if (!fila) return;

        if (e.target.closest('.btn-eliminar')) {
            if (confirm('¿Está seguro de que desea eliminar este registro?')) {
                fila.remove();
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
            document.getElementById('nombreMedico').value = fila.cells[1].innerText;
            document.getElementById('especialidadMedico').value = fila.cells[2].innerText;
            document.getElementById('telefonoMedico').value = fila.cells[3].innerText;
            document.getElementById('correoMedico').value = fila.cells[4].innerText;
            document.getElementById('estadoMedico').value = fila.cells[5].innerText.trim();

            const modalEdicion = new bootstrap.Modal(document.getElementById('modalMedico'));
            modalEdicion.show();
        }
    });
});