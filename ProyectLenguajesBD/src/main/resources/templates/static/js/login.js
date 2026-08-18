// variables
const formLogin = document.getElementById("formLogin");
const usuario = document.getElementById("usuario");
const password = document.getElementById("password");
const mensajeLogin = document.getElementById("mensajeLogin");

//Ejecutable para que funcione el formulario de inicio
formLogin.addEventListener("submit", function (event) {

    //Evita que la página se recargue
    event.preventDefault();

    //Validación de usuario y contraseña
    if (usuario.value === "admin" && password.value === "1234") {

        //Mensaje de inicio correcto
        mensajeLogin.innerHTML = `
            <div class="alert alert-success" role="alert">
                <i class="bi bi-check-circle"></i>
                Inicio de sesión correcto. Bienvenido a Clinic Star.
            </div>
        `;

        //Limpia los campos después del login
        usuario.value = "";
        password.value = "";

        //Redirige al inicio del sistema y hace un tiempo de espera de 2 seg
        setTimeout(function () {
            window.location.href = "index.html";
        }, 2000);

    } else {

        //Mensaje por si hay algun error
        mensajeLogin.innerHTML = `
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-triangle"></i>
                Usuario o contraseña incorrectos.
            </div>
        `;
    }
});