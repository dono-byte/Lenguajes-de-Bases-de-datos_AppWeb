// VARIABLES
const btnNuevaConsulta = document.getElementById("btnNuevaConsulta");
const formConsulta = document.getElementById("formConsulta");
const tablaConsultas = document.getElementById("tablaConsultas");

//Modal de Bootstrap
const modalConsulta = new bootstrap.Modal(
    document.getElementById("modalConsulta")
);

// ABRIR MODAL
btnNuevaConsulta.addEventListener("click", function () {
    //Limpia el formulario
    formConsulta.reset();

    //Abre el modal
    modalConsulta.show();
});

//GUARDAR CONSULTA
formConsulta.addEventListener("submit", function (event) {
    event.preventDefault();

    //Obtener valores
    const fecha = document.getElementById("fechaConsulta").value;
    const medico = document.getElementById("medicoConsulta").value;
    const motivo = document.getElementById("motivoConsulta").value;
    const diagnostico = document.getElementById("diagnosticoConsulta").value;

    //Validar campos
    if (
        fecha === "" ||
        medico === "" ||
        motivo === "" ||
        diagnostico === ""
    ) {
        mostrarMensaje(
            "Todos los campos son obligatorios.",
            "danger"
        );
        return;
    }

    //Crear nueva fila
    const fila = document.createElement("tr");

    fila.innerHTML = `
        <td>${formatearFecha(fecha)}</td>
        <td>${medico}</td>
        <td>${motivo}</td>
        <td>${diagnostico}</td>
        <td class="text-center">
            <button class="btn btn-ver btnVisualizar">
                <i class="bi bi-eye-fill"></i>
            </button>
        </td>
    `;

    //Agregar a la tabla
    tablaConsultas.appendChild(fila);

    //Cerrar modal
    modalConsulta.hide();

    //Limpiar formulario
    formConsulta.reset();

    //Mostrar mensaje
    mostrarMensaje(
        "Consulta registrada correctamente.",
        "success"
    );
});

//VER EL DETALLE DE LA CONSULTA
tablaConsultas.addEventListener("click", function (event) {
    const boton = event.target.closest(".btnVisualizar");
    if (boton) {
        const fila = boton.closest("tr");
        const fecha = fila.cells[0].textContent;
        const medico = fila.cells[1].textContent;
        const motivo = fila.cells[2].textContent;
        const diagnostico = fila.cells[3].textContent;
        alert(
            "Fecha: " + fecha +
            "\nMédico: " + medico +
            "\nMotivo: " + motivo +
            "\nDiagnóstico: " + diagnostico
        );
    }
});

//MENSAJES DE ALERTA
function mostrarMensaje(texto, tipo) {
    const mensajeAnterior = document.getElementById("mensaje");
    if (mensajeAnterior) {
        mensajeAnterior.remove();
    }
    const mensaje = document.createElement("div");
    mensaje.id = "mensaje";
    mensaje.className =
        `alert alert-${tipo} mt-3`;
    mensaje.innerHTML = texto;
    document.querySelector(".col-md-10").prepend(mensaje);
    setTimeout(function () {
        mensaje.remove();
    }, 3000);
}

//FORMATEAR LA FECHA
function formatearFecha(fecha) {
    const partes = fecha.split("-");
    return partes[2] + "/" +
        partes[1] + "/" +
        partes[0];
}