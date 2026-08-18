// public/js/medicamentos.js

document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.getElementById('tablaMedicamentos');
    const btnGuardarMed = document.getElementById('guardarMedicamento');
    const btnAbrirModalNuevo = document.getElementById('btnAbrirModalNuevo');
    const buscarMedicamento = document.getElementById('buscarMedicamento');

    let filaEnEdicion = null;

    // BUSCADOR GENERAL (medicamentos y recetas)
    if (buscarMedicamento) {
        buscarMedicamento.addEventListener('keyup', function () {
            let texto = this.value.toLowerCase();

            tabla.querySelectorAll('tr').forEach(function (fila) {
                let datos = fila.textContent.toLowerCase();
                fila.style.display = datos.includes(texto) ? '' : 'none';
            });

            const tablaRecetas = document.getElementById('tablaRecetas');
            if (tablaRecetas) {
                tablaRecetas.querySelectorAll('tr').forEach(function (fila) {
                    let datos = fila.textContent.toLowerCase();
                    fila.style.display = datos.includes(texto) ? '' : 'none';
                });
            }
        });
    }

    function limpiarFormulario() {
        document.getElementById('nombreMedicamento').value = '';
        document.getElementById('presentacionMedicamento').value = '';
        document.getElementById('concentracionMedicamento').value = '';
        document.getElementById('loteMedicamento').value = '';
        document.getElementById('entradasMedicamento').value = '';
        document.getElementById('salidasMedicamento').value = '';
        document.getElementById('vencimientoMedicamento').value = '';
    }

    if (btnAbrirModalNuevo) {
        btnAbrirModalNuevo.addEventListener('click', function () {
            filaEnEdicion = null;
            document.getElementById('tituloModalMedicamento').innerText = 'Nuevo medicamento';
            limpiarFormulario();
        });
    }

    // Convierte una fecha yyyy-mm-dd (input date) a dd/mm/aaaa para mostrar en la tabla
    function formatearFecha(fecha) {
        if (!fecha) return 'N/A';
        if (fecha.includes('-')) {
            let partes = fecha.split('-');
            return `${partes[2]}/${partes[1]}/${partes[0]}`;
        }
        return fecha;
    }

    if (btnGuardarMed) {
        btnGuardarMed.addEventListener('click', function () {
            const nombre = document.getElementById('nombreMedicamento').value;
            const presentacion = document.getElementById('presentacionMedicamento').value;
            const concentracion = document.getElementById('concentracionMedicamento').value;
            const lote = document.getElementById('loteMedicamento').value;
            const entradas = document.getElementById('entradasMedicamento').value;
            const salidas = document.getElementById('salidasMedicamento').value;
            const vencimiento = formatearFecha(document.getElementById('vencimientoMedicamento').value);

            if (!nombre || !presentacion) {
                alert('Por favor complete al menos el nombre y la presentación.');
                return;
            }

            if (filaEnEdicion) {
                filaEnEdicion.cells[1].innerText = nombre;
                filaEnEdicion.cells[2].innerText = presentacion;
                filaEnEdicion.cells[3].innerText = concentracion || 'N/A';
                filaEnEdicion.cells[4].innerText = entradas || '0';
                filaEnEdicion.cells[5].innerText = salidas || '0';
                filaEnEdicion.cells[6].innerText = lote || 'N/A';
                filaEnEdicion.cells[7].innerText = vencimiento;
            } else {
                const totalFilas = tabla.rows.length + 1;
                const nuevaFila = document.createElement('tr');
                nuevaFila.innerHTML = `
                    <td>${totalFilas}</td>
                    <td>${nombre}</td>
                    <td>${presentacion}</td>
                    <td>${concentracion || 'N/A'}</td>
                    <td>${entradas || '0'}</td>
                    <td>${salidas || '0'}</td>
                    <td>${lote || 'N/A'}</td>
                    <td>${vencimiento}</td>
                    <td class="text-center">
                        <button class="btn btn-info btn-sm btn-ver"><i class="bi bi-eye"></i></button>
                        <button class="btn btn-warning btn-sm btn-editar"><i class="bi bi-pencil"></i></button>
                        <button class="btn btn-danger btn-sm btn-eliminar"><i class="bi bi-trash"></i></button>
                    </td>
                `;
                tabla.appendChild(nuevaFila);
            }

            const modalEl = document.getElementById('modalMedicamento');
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
            document.getElementById('verNombre').innerText = fila.cells[1].innerText;
            document.getElementById('verPresentacion').innerText = fila.cells[2].innerText;
            document.getElementById('verConcentracion').innerText = fila.cells[3].innerText;
            document.getElementById('verEntradas').innerText = fila.cells[4].innerText;
            document.getElementById('verSalidas').innerText = fila.cells[5].innerText;
            document.getElementById('verLote').innerText = fila.cells[6].innerText;
            document.getElementById('verVencimiento').innerText = fila.cells[7].innerText;

            const modalVer = new bootstrap.Modal(document.getElementById('modalVerMedicamento'));
            modalVer.show();
        }

        if (e.target.closest('.btn-editar')) {
            filaEnEdicion = fila;

            document.getElementById('tituloModalMedicamento').innerText = 'Editar medicamento';
            document.getElementById('nombreMedicamento').value = fila.cells[1].innerText;
            document.getElementById('presentacionMedicamento').value = fila.cells[2].innerText;
            document.getElementById('concentracionMedicamento').value = fila.cells[3].innerText;
            document.getElementById('entradasMedicamento').value = fila.cells[4].innerText;
            document.getElementById('salidasMedicamento').value = fila.cells[5].innerText;
            document.getElementById('loteMedicamento').value = fila.cells[6].innerText;
            document.getElementById('vencimientoMedicamento').value = '';

            const modalEdicion = new bootstrap.Modal(document.getElementById('modalMedicamento'));
            modalEdicion.show();
        }
    });
});