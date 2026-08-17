// Variables
const buscarPaciente = document.getElementById("buscarPaciente");
const btnNuevoPaciente = document.getElementById("btnNuevoPaciente");
const tablaPacientes = document.querySelector("tbody");

// Variable para guardar la fila que se está editando
let filaActual = null;

// BUSCADOR GENERAL DE PACIENTES
buscarPaciente.addEventListener("keyup", function () {
    let texto = this.value.toLowerCase();
    let filas = tablaPacientes.querySelectorAll("tr");
    filas.forEach(function (fila) {
        let datos = fila.textContent.toLowerCase();
        fila.style.display = datos.includes(texto)
            ? ""
            : "none";
    });
});

// NUEVO PACIENTE
document.getElementById("guardarPaciente")
.addEventListener("click", function () {

    //Obtener datos del formulario
    let cedula = document.getElementById("cedula").value;
    let nombre = document.getElementById("nombre").value;
    let apellido = document.getElementById("apellido").value;
    let telefono = document.getElementById("telefono").value;
    let correo = document.getElementById("correo").value;

    //Validación básica
    if (
        cedula === "" ||
        nombre === "" ||
        apellido === "" ||
        telefono === "" ||
        correo === ""
    ) {

        alert("Debe completar todos los campos");
        return;
    }

    //Crea una nueva fila en la tabla
    let fila = tablaPacientes.insertRow();

    fila.innerHTML = `
        <td>${tablaPacientes.rows.length}</td>
        <td>${cedula}</td>
        <td>${nombre}</td>
        <td>${apellido}</td>
        <td>${telefono}</td>
        <td>${correo}</td>
        <td>
            <span class="badge bg-success">
                Activo
            </span>
        </td>
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

    //Limpiar formulario
    document.getElementById("formPaciente").reset();

    //Cerrar modal
    bootstrap.Modal.getInstance(
        document.getElementById("modalNuevoPaciente")
    ).hide();
});

//BOTONES GENERALES DE ACCIONES
document.addEventListener("click", function (event) {
    let boton = event.target.closest(".accion-btn");
    if (!boton) return;
    let accion = boton.dataset.action;
    let fila = boton.closest("tr");
    switch (accion) {

        //VER PACIENTE
        case "ver":
            verPaciente(fila);
        break;

        //EDITAR PACIENTE
        case "editar":
            editarPaciente(fila);
        break;

        //ELIMINAR PACIENTE
        case "eliminar":
            let paciente =
                fila.children[2].textContent
                +
                " "
                +
                fila.children[3].textContent;
            let confirmar = confirm(
                "¿Desea eliminar a "
                + paciente
                + "?"
            );
            if(confirmar){
                fila.remove();
                alert(
                    "Paciente eliminado correctamente"
                );
            }
        break;
    }
});

// MOSTRAR INFORMACIÓN DEL PACIENTE
function verPaciente(fila){
    document.getElementById("verCedula").textContent =
        fila.children[1].textContent;
    document.getElementById("verNombre").textContent =
        fila.children[2].textContent;
    document.getElementById("verApellido").textContent =
        fila.children[3].textContent;
    document.getElementById("verTelefono").textContent =
        fila.children[4].textContent;
    document.getElementById("verCorreo").textContent =
        fila.children[5].textContent;
    document.getElementById("verEstado").innerHTML =
        fila.children[6].innerHTML;
    let modal = new bootstrap.Modal(
        document.getElementById("modalVerPaciente")
    );
    modal.show();
}

// CARGAR DATOS PARA EDITAR
function editarPaciente(fila){
    filaActual = fila;
    document.getElementById("editarNombre").value =
        fila.children[2].textContent;
    document.getElementById("editarApellido").value =
        fila.children[3].textContent;
    document.getElementById("editarTelefono").value =
        fila.children[4].textContent;
    document.getElementById("editarCorreo").value =
        fila.children[5].textContent;
    let modal = new bootstrap.Modal(
        document.getElementById("modalEditarPaciente")
    );
    modal.show();
}

// GUARDAR CAMBIOS DE EDICIÓN
document.getElementById("actualizarPaciente")
.addEventListener("click", function(){
    filaActual.children[2].textContent =
        document.getElementById("editarNombre").value;
    filaActual.children[3].textContent =
        document.getElementById("editarApellido").value;
    filaActual.children[4].textContent =
        document.getElementById("editarTelefono").value;
    filaActual.children[5].textContent =
        document.getElementById("editarCorreo").value;
    bootstrap.Modal.getInstance(
        document.getElementById("modalEditarPaciente")
    ).hide();
    alert(
        "Paciente actualizado correctamente"
    );
});