//Variables 
const buscarCita = document.getElementById("buscarCita");
const tablaCitas = document.querySelector("tbody");

//Guarda la fila que se está editando
let filaActual = null;

//BUSCADOR GENERAL DE CITA
buscarCita.addEventListener("keyup", function () {
    let texto = this.value.toLowerCase();
    let filas = tablaCitas.querySelectorAll("tr");
    filas.forEach(function (fila) {
        let datos = fila.textContent.toLowerCase();
        fila.style.display = datos.includes(texto)
            ? ""
            : "none";
    });
});

//CREAR NUEVA CITA
document.getElementById("guardarCita")
    .addEventListener("click", function () {

        //Obtiene los datos del formulario
        let paciente =
            document.getElementById("paciente").value;
        let fecha =
            document.getElementById("fecha").value;
        let hora =
            document.getElementById("hora").value;
        let medico =
            document.getElementById("medico").value;
        let estado =
            document.getElementById("estado").value;

        //Validación
        if (
            paciente === "" ||
            fecha === "" ||
            hora === "" ||
            medico === ""
        ) {
            alert("Debe completar todos los campos");
            return;
        }

        // Crear nueva fila
        let fila = tablaCitas.insertRow();
        let estadoClase = obtenerClaseEstado(estado);
        fila.innerHTML = `
    <td>${tablaCitas.rows.length}</td>
    <td>${paciente}</td>
    <td>${fecha}</td>
    <td>${hora}</td>
    <td>${medico}</td>
    <td>
        <span class="badge ${estadoClase}">
            ${estado}
        </span>
    </td>
    <td class="text-center">
        <button 
        class="btn btn-info btn-sm accion-btn"
        data-action="ver">
            <i class="bi bi-eye"></i>
        </button>
        <button 
        class="btn btn-warning btn-sm accion-btn"
        data-action="editar">
            <i class="bi bi-pencil"></i>
        </button>
        <button 
        class="btn btn-danger btn-sm accion-btn"
        data-action="eliminar">
            <i class="bi bi-trash"></i>
        </button>
    </td>
    `;

        //Limpiar formulario
        document.getElementById("paciente").value = "";
        document.getElementById("fecha").value = "";
        document.getElementById("hora").value = "";
        document.getElementById("medico").value = "";

        //Cerrar modal
        bootstrap.Modal.getInstance(
            document.getElementById("modalNuevaCita")
        ).hide();
    });

//BOTONES GENERALES
document.addEventListener("click", function (event) {
    let boton = event.target.closest(".accion-btn");
    if (!boton) return;
    let accion = boton.dataset.action;
    let fila = boton.closest("tr");
    switch (accion) {

        //VER
        case "ver":
            verCita(fila);
            break;

        //EDITAR
        case "editar":
            editarCita(fila);
            break;

        // ELIMINAR
        case "eliminar":
            let paciente =
                fila.children[1].textContent;
            let confirmar = confirm(
                "¿Desea eliminar la cita de "
                + paciente
                + "?"
            );
            if (confirmar) {
                fila.remove();
                alert(
                    "Cita eliminada correctamente"
                );
            }
            break;
    }
});

//MUESTRA LA INFORMACIÓN DE  LA CITA
function verCita(fila) {
    document.getElementById("verPaciente").textContent =
        fila.children[1].textContent;
    document.getElementById("verFecha").textContent =
        fila.children[2].textContent;
    document.getElementById("verHora").textContent =
        fila.children[3].textContent;
    document.getElementById("verMedico").textContent =
        fila.children[4].textContent;
    document.getElementById("verEstado").innerHTML =
        fila.children[5].innerHTML;
    let modal = new bootstrap.Modal(
        document.getElementById("modalVerCita")
    );
    modal.show();
}

//CARGAR DATOS PARA EDITAR
function editarCita(fila) {
    filaActual = fila;
    document.getElementById("editarPaciente").value =
        fila.children[1].textContent;
    document.getElementById("editarFecha").value =
        fila.children[2].textContent;
    document.getElementById("editarHora").value =
        fila.children[3].textContent;
    document.getElementById("editarMedico").value =
        fila.children[4].textContent;
    document.getElementById("editarEstado").value =
        fila.children[5].textContent.trim();
    let modal = new bootstrap.Modal(
        document.getElementById("modalEditarCita")
    );
    modal.show();
}

//GUARDAR CAMBIOS DE EDICIÓN
document.getElementById("actualizarCita")
    .addEventListener("click", function () {
        let estado =
            document.getElementById("editarEstado").value;
        filaActual.children[1].textContent =
            document.getElementById("editarPaciente").value;
        filaActual.children[2].textContent =
            document.getElementById("editarFecha").value;
        filaActual.children[3].textContent =
            document.getElementById("editarHora").value;
        filaActual.children[4].textContent =
            document.getElementById("editarMedico").value;
        filaActual.children[5].innerHTML = `
        <span class="badge ${obtenerClaseEstado(estado)}">
            ${estado}
        </span>
    `;

        bootstrap.Modal.getInstance(
            document.getElementById("modalEditarCita")
        ).hide();

        alert(
            "Cita actualizada correctamente"
        );

    });

//CLASES PARA ESTADOS
function obtenerClaseEstado(estado) {
    switch (estado) {
        case "Pendiente":
            return "bg-warning text-dark";
        case "Confirmada":
            return "bg-success";
        case "Cancelada":
            return "bg-danger";
        default:
            return "bg-secondary";
    }
}