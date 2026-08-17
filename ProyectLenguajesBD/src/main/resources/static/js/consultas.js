// Variables
const buscarPaciente = document.getElementById("buscarPaciente");
const btnNuevaConsulta = document.getElementById("btnNuevaConsulta");
const tablaConsultas = document.getElementById("tablaConsultas");

// Variable para guardar la fila que se está editando
let filaActual = null;

// BUSCADOR GENERAL DE CONSULTAS
buscarPaciente.addEventListener("keyup", function () {
    let texto = this.value.toLowerCase();
    let filas = tablaConsultas.querySelectorAll("tr");

    filas.forEach(function (fila) {
        let datos = fila.textContent.toLowerCase();
        fila.style.display = datos.includes(texto)
            ? ""
            : "none";
    });
});

// NUEVA CONSULTA
document.getElementById("guardarConsulta")
.addEventListener("click", function () {

    // Obtener datos del formulario
    let fecha = document.getElementById("fechaConsulta").value;
    let paciente = document.getElementById("pacienteConsulta").value;
    let medico = document.getElementById("doctorConsulta").value;
    let motivo = document.getElementById("motivoConsulta").value;
    let diagnostico = document.getElementById("diagnosticoConsulta").value;

    // Validación básica
    if (
        fecha === "" || paciente === "" || medico === "" || motivo === "" || diagnostico === ""
    ) {
        alert("Debe completar todos los campos");
        return;
    }

    // Crear nueva fila
    let fila = tablaConsultas.insertRow();
    fila.innerHTML = `
        <td>${fecha}</td>
        <td>${paciente}</td>
        <td>${medico}</td>
        <td>${motivo}</td>
        <td>${diagnostico}</td>

        <td class="text-center">

            <button class="btn btn-info btn-sm accion-btn"
            data-action="ver">
                <i class="bi bi-eye"></i>
            </button>

            <button class="btn btn-warning btn-sm accion-btn"
            data-action="editar">
                <i class="bi bi-pencil-square"></i>
            </button>

            <button class="btn btn-danger btn-sm accion-btn"
            data-action="eliminar">
                <i class="bi bi-trash"></i>
            </button>
        </td>
    `;

    // Limpiar formulario
    document.getElementById("formConsulta").reset();

    // Cerrar modal
    bootstrap.Modal.getInstance(
        document.getElementById("modalNuevaConsulta")
    ).hide();
});

// BOTONES GENERALES
document.addEventListener("click", function (event) {
    let boton = event.target.closest(".accion-btn");
    if (!boton) return;
    let accion = boton.dataset.action;
    let fila = boton.closest("tr");

    switch (accion) {

        // VER CONSULTA
        case "ver":
            verConsulta(fila);
        break;

        // EDITAR CONSULTA
        case "editar":
            editarConsulta(fila);
        break;

        // ELIMINAR CONSULTA
        case "eliminar":
            let paciente = fila.children[1].textContent;

            let confirmar = confirm(
                "¿Desea eliminar la consulta de "
                + paciente +
                "?"
            );

            if (confirmar) {

                fila.remove();

                alert(
                    "Consulta eliminada correctamente"
                );
            }
        break;
    }
});

// VER CONSULTA
function verConsulta(fila){

    document.getElementById("verFecha").textContent =
        fila.children[0].textContent;

    document.getElementById("verPaciente").textContent =
        fila.children[1].textContent;

    document.getElementById("verDoctor").textContent =
        fila.children[2].textContent;

    document.getElementById("verMotivo").textContent =
        fila.children[3].textContent;

    document.getElementById("verDiagnostico").textContent =
        fila.children[4].textContent;

    let modal = new bootstrap.Modal(
        document.getElementById("modalVerConsulta")
    );
    modal.show();
}

// CARGAR DATOS PARA EDITAR
function editarConsulta(fila){
    filaActual = fila;

    document.getElementById("editarPaciente").value =
        fila.children[1].textContent;

    document.getElementById("editarDoctor").value =
        fila.children[2].textContent;

    document.getElementById("editarMotivo").value =
        fila.children[3].textContent;

    document.getElementById("editarDiagnostico").value =
        fila.children[4].textContent;

    let modal = new bootstrap.Modal(
        document.getElementById("modalEditarConsulta")
    );
    modal.show();
}

//GUARDAR CAMBIOS
document.getElementById("actualizarConsulta")
.addEventListener("click", function(){

    filaActual.children[1].textContent =
        document.getElementById("editarPaciente").value;

    filaActual.children[2].textContent =
        document.getElementById("editarDoctor").value;

    filaActual.children[3].textContent =
        document.getElementById("editarMotivo").value;

    filaActual.children[4].textContent =
        document.getElementById("editarDiagnostico").value;

    bootstrap.Modal.getInstance(
        document.getElementById("modalEditarConsulta")
    ).hide();

    alert(
        "Consulta actualizada correctamente"
    );
});