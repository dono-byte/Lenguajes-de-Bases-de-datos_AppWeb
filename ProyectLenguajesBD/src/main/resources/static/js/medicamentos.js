 // public/js/medicamentos.js

document.addEventListener('DOMContentLoaded', function () {
    const tabla = document.querySelector('table tbody');
    const btnGuardarMed = document.getElementById('guardarMedicamento');
    const btnAbrirModalNuevo = document.getElementById('btnAbrirModalNuevo');
    
    let filaEnEdicion = null;

    if (btnAbrirModalNuevo) {
        btnAbrirModalNuevo.addEventListener('click', function () {
            filaEnEdicion = null;
            document.getElementById('tituloModalMedicamento').innerText = 'Nuevo medicamento';
            document.getElementById('nombreMedicamento').value = '';
            document.getElementById('stockMedicamento').value = '';
            document.getElementById('precioMedicamento').value = '';
            document.getElementById('descripcionMedicamento').value = '';
        });
    }

    if (btnGuardarMed) {
        btnGuardarMed.addEventListener('click', function () {
            const nombre = document.getElementById('nombreMedicamento').value;
            const stock = document.getElementById('stockMedicamento').value;
            const precio = document.getElementById('precioMedicamento').value;
            const descripcion = document.getElementById('descripcionMedicamento').value;

            if (!nombre || !stock) {
                alert('Por favor complete al menos el nombre y el stock.');
                return;
            }

            if (filaEnEdicion) {
                filaEnEdicion.cells[1].innerText = nombre;
                filaEnEdicion.cells[2].innerText = stock;
                filaEnEdicion.cells[3].innerText = `$${precio || '0.00'}`;
                filaEnEdicion.cells[4].innerText = descripcion || 'N/A';
            } else {
                const totalFilas = tabla.rows.length + 1;
                const nuevaFila = document.createElement('tr');
                nuevaFila.innerHTML = `
                    <td>${totalFilas}</td>
                    <td>${nombre}</td>
                    <td>${stock}</td>
                    <td>$${precio || '0.00'}</td>
                    <td>${descripcion || 'N/A'}</td>
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
            document.getElementById('verStock').innerText = fila.cells[2].innerText;
            document.getElementById('verPrecio').innerText = fila.cells[3].innerText;
            document.getElementById('verDescripcion').innerText = fila.cells[4].innerText;

            const modalVer = new bootstrap.Modal(document.getElementById('modalVerMedicamento'));
            modalVer.show();
        }

        if (e.target.closest('.btn-editar')) {
            filaEnEdicion = fila;

            document.getElementById('tituloModalMedicamento').innerText = 'Editar medicamento';
            document.getElementById('nombreMedicamento').value = fila.cells[1].innerText;
            document.getElementById('stockMedicamento').value = fila.cells[2].innerText;
            document.getElementById('precioMedicamento').value = fila.cells[3].innerText.replace('$', '');
            document.getElementById('descripcionMedicamento').value = fila.cells[4].innerText;

            const modalEdicion = new bootstrap.Modal(document.getElementById('modalMedicamento'));
            modalEdicion.show();
        }
    });
});

