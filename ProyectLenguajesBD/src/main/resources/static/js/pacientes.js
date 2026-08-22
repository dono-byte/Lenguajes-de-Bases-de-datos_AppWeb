document.addEventListener('DOMContentLoaded', () => {
    const buscarPaciente = document.getElementById('buscarPaciente');
    const tablaPacientes = document.querySelector('table tbody');
    let pacienteEnEdicion = null;

    // ========== FIJAR FECHA MÁXIMA (HOY) ==========
    const hoy = new Date();
    const año = hoy.getFullYear();
    const mes = String(hoy.getMonth() + 1).padStart(2, '0');
    const dia = String(hoy.getDate()).padStart(2, '0');
    const fechaMax = `${año}-${mes}-${dia}`;

    const inputsFecha = ['fechaNacimiento', 'editarFechaNacimiento'];
    inputsFecha.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.setAttribute('max', fechaMax);
        }
    });

    // ========== FUNCIONES DE VALIDACIÓN ==========
    function soloNumeros(valor) {
        return /^\d+$/.test(valor);
    }

    function validarCedula(cedula) {
        return cedula && cedula.length === 9 && soloNumeros(cedula);
    }

    function validarTelefono(telefono) {
        if (telefono === '') return true;
        return telefono.length === 8 && soloNumeros(telefono);
    }

    function validarFechaNacimiento(fecha) {
        if (!fecha) return false;
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);
        const nacimiento = new Date(fecha);
        return nacimiento <= hoy;
    }

    // ========== FILTRAR SOLO NÚMEROS EN TIEMPO REAL ==========
    function filtrarSoloNumeros(event) {
        const input = event.target;
        const valor = input.value;
        const soloDigitos = valor.replace(/\D/g, '');
        if (valor !== soloDigitos) {
            input.value = soloDigitos;
        }
    }

    const camposNumericos = ['cedula', 'telefono', 'editarTelefono'];
    camposNumericos.forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', filtrarSoloNumeros);
        }
    });

    // ========== CARGAR PACIENTES ==========
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

    cargarPacientes();

    // ========== BUSCADOR ==========
    buscarPaciente.addEventListener('keyup', function () {
        const texto = this.value.toLowerCase();
        tablaPacientes.querySelectorAll('tr').forEach(fila => {
            fila.style.display = fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
        });
    });

    // ========== NUEVO PACIENTE (abrir modal) ==========
    document.getElementById('btnNuevoPaciente').addEventListener('click', () => {
        pacienteEnEdicion = null;
        document.getElementById('formPaciente').reset();
        // Resetear select de género a la primera opción
        document.getElementById('genero').selectedIndex = 0;
    });

    // ========== GUARDAR NUEVO PACIENTE ==========
    document.getElementById('guardarPaciente').addEventListener('click', async () => {
        const cedula = document.getElementById('cedula').value.trim();
        const nombre = document.getElementById('nombre').value.trim();
        const fechaNacimiento = document.getElementById('fechaNacimiento').value;
        const genero = document.getElementById('genero').value;
        const telefono = document.getElementById('telefono').value.trim();
        const direccion = document.getElementById('direccion').value.trim();

        if (!cedula || !nombre || !fechaNacimiento || !genero) {
            alert('Complete todos los campos obligatorios (*)');
            return;
        }

        if (!validarCedula(cedula)) {
            alert('La cédula debe tener exactamente 9 dígitos numéricos.');
            return;
        }

        if (!validarTelefono(telefono)) {
            alert('El teléfono, si se ingresa, debe tener exactamente 8 dígitos numéricos.');
            return;
        }

        if (!validarFechaNacimiento(fechaNacimiento)) {
            alert('La fecha de nacimiento no puede ser futura. La persona ya debe haber nacido.');
            return;
        }

        const datos = {
            cedula,
            nombre,
            fechaNacimiento,
            genero,
            telefono,
            direccion
        };

        await fetch('/api/pacientes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        bootstrap.Modal.getInstance(document.getElementById('modalNuevoPaciente')).hide();
        await cargarPacientes();
    });

    // ========== EVENTOS EN TABLA (VER, EDITAR, ELIMINAR) ==========
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
        }
        else if (boton.dataset.action === 'editar') {
            pacienteEnEdicion = fila;
            document.getElementById('editarNombre').value = fila.cells[2].textContent;
            document.getElementById('editarFechaNacimiento').value = fila.cells[3].textContent;
            // Cargar género en el select
            const generoTexto = fila.cells[4].textContent.trim();
            const selectGenero = document.getElementById('editarGenero');
            // Buscar la opción que coincida (puede ser "Hombre", "Mujer", "M", "F" según tu base de datos)
            // Ajustamos para que funcione con ambos casos
            let opcionEncontrada = false;
            for (let option of selectGenero.options) {
                if (option.value === generoTexto || option.text === generoTexto) {
                    selectGenero.value = option.value;
                    opcionEncontrada = true;
                    break;
                }
            }
            if (!opcionEncontrada) {
                // Si no coincide, dejar seleccionado el primero (por defecto)
                selectGenero.selectedIndex = 0;
            }
            document.getElementById('editarTelefono').value = fila.cells[5].textContent;
            document.getElementById('editarDireccion').value = fila.cells[6].textContent;
            new bootstrap.Modal(document.getElementById('modalEditarPaciente')).show();
        }
        else if (boton.dataset.action === 'eliminar') {
            if (confirm('¿Eliminar este paciente?')) {
                await fetch(`/api/pacientes/${fila.dataset.id}`, { method: 'DELETE' });
                await cargarPacientes();
            }
        }
    });

    // ========== ACTUALIZAR PACIENTE ==========
    document.getElementById('actualizarPaciente').addEventListener('click', async () => {
        const nombre = document.getElementById('editarNombre').value.trim();
        const fechaNacimiento = document.getElementById('editarFechaNacimiento').value;
        const genero = document.getElementById('editarGenero').value;
        const telefono = document.getElementById('editarTelefono').value.trim();
        const direccion = document.getElementById('editarDireccion').value.trim();

        if (!nombre || !fechaNacimiento || !genero) {
            alert('Complete todos los campos obligatorios (*)');
            return;
        }

        if (!validarTelefono(telefono)) {
            alert('El teléfono, si se ingresa, debe tener exactamente 8 dígitos numéricos.');
            return;
        }

        if (!validarFechaNacimiento(fechaNacimiento)) {
            alert('La fecha de nacimiento no puede ser futura. La persona ya debe haber nacido.');
            return;
        }

        const datos = {
            cedula: pacienteEnEdicion.cells[1].textContent,
            nombre,
            fechaNacimiento,
            genero,
            telefono,
            direccion
        };

        await fetch(`/api/pacientes/${pacienteEnEdicion.dataset.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        bootstrap.Modal.getInstance(document.getElementById('modalEditarPaciente')).hide();
        await cargarPacientes();
    });
});