// Variables
const formLogin = document.getElementById("formLogin");
const mensajeLogin = document.getElementById("mensajeLogin");

// Escuchar el envío del formulario
formLogin.addEventListener("submit", function (event) {
    // NO llamamos a preventDefault() para que el formulario se envíe al servidor
    // Mostrar un mensaje de espera mientras se procesa la petición
    mensajeLogin.innerHTML = `
        <div class="alert alert-info" role="alert">
            <i class="bi bi-hourglass-split"></i> Validando credenciales...
        </div>
    `;
});