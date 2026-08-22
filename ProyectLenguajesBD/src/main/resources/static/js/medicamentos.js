// public/js/medicamentos.js

document.addEventListener("DOMContentLoaded", function () {
  const tabla = document.getElementById("tablaMedicamentos");
  const btnGuardarMed = document.getElementById("guardarMedicamento");
  const btnAbrirModalNuevo = document.getElementById("btnAbrirModalNuevo");
  const buscarMedicamento = document.getElementById("buscarMedicamento");
  const tablaRecetas = document.getElementById("tablaRecetas");
  const pacienteAsignacion = document.getElementById("pacienteAsignacion");
  const consultaAsignacion = document.getElementById("consultaAsignacion");

  let medicamentoEnEdicion = null;
  let asignacionEnEdicion = null;

  cargarMedicamentos();
  cargarAsignaciones();

  async function cargarAsignaciones() {
    const respuesta = await fetch("/api/asignaciones-medicamentos");
    const asignaciones = await respuesta.json();
    tablaRecetas.innerHTML = asignaciones
      .map(
        (
          asignacion,
        ) => `<tr data-id="${asignacion.id}" data-paciente-id="${asignacion.pacienteId}" data-consulta-id="${asignacion.consultaId}" data-medicamento-id="${asignacion.medicamentoId}">
            <td>${asignacion.paciente}</td><td>${asignacion.medicamento}</td>
            <td>${asignacion.dosis || ""}</td><td>${asignacion.frecuencia || ""}</td>
            <td>Consulta #${asignacion.consultaId}</td>
            <td class="text-center"><button class="btn btn-warning btn-sm btn-editar-asignacion"><i class="bi bi-pencil"></i></button>
            <button class="btn btn-danger btn-sm btn-eliminar-asignacion"><i class="bi bi-trash"></i></button></td>
        </tr>`,
      )
      .join("");
  }

  async function cargarMedicamentos() {
    const respuesta = await fetch("/api/medicamentos");
    const medicamentos = await respuesta.json();
    tabla.innerHTML = medicamentos.map(crearFila).join("");
  }

  function crearFila(medicamento) {
    const fecha = medicamento.vencimiento || "";
    return `<tr data-id="${medicamento.id}">
            <td>${medicamento.id}</td><td>${medicamento.nombre}</td>
            <td>${medicamento.presentacion || "N/A"}</td><td>${medicamento.concentracion || "N/A"}</td>
            <td>${medicamento.entradas ?? 0}</td><td>${medicamento.salidas ?? 0}</td>
            <td>${medicamento.lote || "N/A"}</td><td>${formatearFecha(fecha)}</td>
            <td class="text-center">
                <button class="btn btn-info btn-sm btn-ver"><i class="bi bi-eye"></i></button>
                <button class="btn btn-warning btn-sm btn-editar"><i class="bi bi-pencil"></i></button>
                <button class="btn btn-danger btn-sm btn-eliminar"><i class="bi bi-trash"></i></button>
            </td>
        </tr>`;
  }

  // BUSCADOR GENERAL (medicamentos y recetas)
  if (buscarMedicamento) {
    buscarMedicamento.addEventListener("keyup", function () {
      let texto = this.value.toLowerCase();

      tabla.querySelectorAll("tr").forEach(function (fila) {
        let datos = fila.textContent.toLowerCase();
        fila.style.display = datos.includes(texto) ? "" : "none";
      });

      if (tablaRecetas) {
        tablaRecetas.querySelectorAll("tr").forEach(function (fila) {
          let datos = fila.textContent.toLowerCase();
          fila.style.display = datos.includes(texto) ? "" : "none";
        });
      }
    });
  }

  function limpiarFormulario() {
    document.getElementById("nombreMedicamento").value = "";
    document.getElementById("presentacionMedicamento").value = "";
    document.getElementById("concentracionMedicamento").value = "";
    document.getElementById("loteMedicamento").value = "";
    document.getElementById("entradasMedicamento").value = "";
    document.getElementById("salidasMedicamento").value = "";
    document.getElementById("vencimientoMedicamento").value = "";
  }

  if (btnAbrirModalNuevo) {
    btnAbrirModalNuevo.addEventListener("click", function () {
      medicamentoEnEdicion = null;
      document.getElementById("tituloModalMedicamento").innerText =
        "Nuevo medicamento";
      limpiarFormulario();
    });
  }

  if (pacienteAsignacion) {
    document
      .getElementById("btnAbrirModalAsignar")
      .addEventListener("click", () => {
        asignacionEnEdicion = null;
        document.querySelector(
          "#modalAsignarMedicamento .modal-title",
        ).textContent = "Asignar medicamento a paciente";
        document.getElementById("guardarAsignacion").textContent =
          "Asignar medicamento";
        pacienteAsignacion.value = "";
        consultaAsignacion.value = "";
        document.getElementById("medicamentoAsignacion").value = "";
        document.getElementById("dosisAsignacion").value = "";
        document.getElementById("frecuenciaAsignacion").value = "";
        Array.from(consultaAsignacion.options).forEach(
          (opcion) => (opcion.hidden = false),
        );
      });
    pacienteAsignacion.addEventListener("change", () => {
      const pacienteId = pacienteAsignacion.value;
      Array.from(consultaAsignacion.options).forEach((opcion) => {
        opcion.hidden =
          opcion.value !== "" && opcion.dataset.pacienteId !== pacienteId;
      });
      consultaAsignacion.value = "";
    });
  }

  document
    .getElementById("guardarAsignacion")
    .addEventListener("click", async () => {
      const datos = {
        pacienteId: Number(pacienteAsignacion.value),
        consultaId: Number(consultaAsignacion.value),
        medicamentoId: Number(
          document.getElementById("medicamentoAsignacion").value,
        ),
        dosis: document.getElementById("dosisAsignacion").value,
        frecuencia: document.getElementById("frecuenciaAsignacion").value,
      };
      if (
        !datos.pacienteId ||
        !datos.consultaId ||
        !datos.medicamentoId ||
        !datos.dosis ||
        !datos.frecuencia
      ) {
        alert("Complete paciente, consulta, medicamento, dosis y frecuencia.");
        return;
      }
      const respuesta = await fetch(
        asignacionEnEdicion
          ? `/api/asignaciones-medicamentos/${asignacionEnEdicion.dataset.id}`
          : "/api/asignaciones-medicamentos",
        {
          method: asignacionEnEdicion ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(datos),
        },
      );
      if (!respuesta.ok) {
        alert("No se pudo asignar el medicamento.");
        return;
      }
      await cargarAsignaciones();
      asignacionEnEdicion = null;
      document.getElementById("pacienteAsignacion").value = "";
      document.getElementById("consultaAsignacion").value = "";
      document.getElementById("medicamentoAsignacion").value = "";
      document.getElementById("dosisAsignacion").value = "";
      document.getElementById("frecuenciaAsignacion").value = "";
      bootstrap.Modal.getInstance(
        document.getElementById("modalAsignarMedicamento"),
      ).hide();
    });

  tablaRecetas.addEventListener("click", async (event) => {
    const boton = event.target.closest("button");
    if (!boton) return;
    const fila = boton.closest("tr");
    if (boton.classList.contains("btn-eliminar-asignacion")) {
      if (confirm("¿Eliminar esta asignación?")) {
        const respuesta = await fetch(
          `/api/asignaciones-medicamentos/${fila.dataset.id}`,
          { method: "DELETE" },
        );
        if (!respuesta.ok) {
          alert("No se pudo eliminar la asignación.");
          return;
        }
        await cargarAsignaciones();
      }
    }
    if (boton.classList.contains("btn-editar-asignacion")) {
      asignacionEnEdicion = fila;
      document.getElementById("pacienteAsignacion").value =
        fila.dataset.pacienteId;
      pacienteAsignacion.dispatchEvent(new Event("change"));
      document.getElementById("consultaAsignacion").value =
        fila.dataset.consultaId;
      document.getElementById("medicamentoAsignacion").value =
        fila.dataset.medicamentoId;
      document.getElementById("dosisAsignacion").value =
        fila.cells[2].textContent;
      document.getElementById("frecuenciaAsignacion").value =
        fila.cells[3].textContent;
      document.querySelector(
        "#modalAsignarMedicamento .modal-title",
      ).textContent = "Editar asignación";
      document.getElementById("guardarAsignacion").textContent =
        "Guardar cambios";
      new bootstrap.Modal(
        document.getElementById("modalAsignarMedicamento"),
      ).show();
    }
  });

  // Convierte una fecha yyyy-mm-dd (input date) a dd/mm/aaaa para mostrar en la tabla
  function formatearFecha(fecha) {
    if (!fecha) return "N/A";
    if (fecha.includes("-")) {
      let partes = fecha.split("-");
      return `${partes[2]}/${partes[1]}/${partes[0]}`;
    }
    return fecha;
  }

  if (btnGuardarMed) {
    btnGuardarMed.addEventListener("click", async function () {
      // Obtener valores del formulario
      const nombre = document.getElementById("nombreMedicamento").value.trim();
      const presentacion = document
        .getElementById("presentacionMedicamento")
        .value.trim();
      const concentracion = document
        .getElementById("concentracionMedicamento")
        .value.trim();
      const lote = document.getElementById("loteMedicamento").value.trim();
      const entradasVal = document.getElementById("entradasMedicamento").value;
      const salidasVal = document.getElementById("salidasMedicamento").value;
      const vencimiento =
        document.getElementById("vencimientoMedicamento").value || null;

      // Validar campos obligatorios
      if (!nombre || !presentacion) {
        alert("Por favor complete al menos el nombre y la presentación.");
        return;
      }

      // Convertir a número, permitiendo vacío (se tratará como null)
      const entradas = entradasVal === "" ? null : Number(entradasVal);
      const salidas = salidasVal === "" ? null : Number(salidasVal);

      // Validar que no sean negativos
      if (entradas !== null && entradas < 0) {
        alert("Las entradas no pueden ser negativas.");
        return;
      }
      if (salidas !== null && salidas < 0) {
        alert("Las salidas no pueden ser negativas.");
        return;
      }

      // Validar que salidas no sean mayores que entradas (si ambos existen)
      if (entradas !== null && salidas !== null && salidas > entradas) {
        alert("Las salidas no pueden ser mayores que las entradas.");
        return;
      }

      // Construir objeto con los datos
      const datos = {
        nombre,
        presentacion,
        concentracion,
        entradas, // puede ser null
        salidas, // puede ser null
        lote,
        vencimiento,
      };

      try {
        const url = medicamentoEnEdicion
          ? `/api/medicamentos/${medicamentoEnEdicion.dataset.id}`
          : "/api/medicamentos";
        const response = await fetch(url, {
          method: medicamentoEnEdicion ? "PUT" : "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(datos),
        });

        if (!response.ok) {
          const errorMsg = await response.text();
          alert("Error al guardar: " + errorMsg);
          return;
        }

        // Recargar tabla y cerrar modal
        await cargarMedicamentos();
        const modalEl = document.getElementById("modalMedicamento");
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();
      } catch (error) {
        alert("Error de conexión: " + error.message);
      }
    });
  }

  tabla.addEventListener("click", async function (e) {
    const fila = e.target.closest("tr");
    if (!fila) return;

    if (e.target.closest(".btn-eliminar")) {
      if (confirm("¿Está seguro de que desea eliminar este registro?")) {
        await fetch(`/api/medicamentos/${fila.dataset.id}`, {
          method: "DELETE",
        });
        await cargarMedicamentos();
      }
    }

    if (e.target.closest(".btn-ver")) {
      document.getElementById("verNombre").innerText = fila.cells[1].innerText;
      document.getElementById("verPresentacion").innerText =
        fila.cells[2].innerText;
      document.getElementById("verConcentracion").innerText =
        fila.cells[3].innerText;
      document.getElementById("verEntradas").innerText =
        fila.cells[4].innerText;
      document.getElementById("verSalidas").innerText = fila.cells[5].innerText;
      document.getElementById("verLote").innerText = fila.cells[6].innerText;
      document.getElementById("verVencimiento").innerText =
        fila.cells[7].innerText;

      const modalVer = new bootstrap.Modal(
        document.getElementById("modalVerMedicamento"),
      );
      modalVer.show();
    }

    if (e.target.closest(".btn-editar")) {
      medicamentoEnEdicion = fila;

      document.getElementById("tituloModalMedicamento").innerText =
        "Editar medicamento";
      document.getElementById("nombreMedicamento").value =
        fila.cells[1].innerText;
      document.getElementById("presentacionMedicamento").value =
        fila.cells[2].innerText;
      document.getElementById("concentracionMedicamento").value =
        fila.cells[3].innerText;
      document.getElementById("entradasMedicamento").value =
        fila.cells[4].innerText;
      document.getElementById("salidasMedicamento").value =
        fila.cells[5].innerText;
      document.getElementById("loteMedicamento").value =
        fila.cells[6].innerText;
      const fecha = fila.cells[7].innerText;
      document.getElementById("vencimientoMedicamento").value = fecha.includes(
        "/",
      )
        ? fecha.split("/").reverse().join("-")
        : "";

      const modalEdicion = new bootstrap.Modal(
        document.getElementById("modalMedicamento"),
      );
      modalEdicion.show();
    }
  });
});