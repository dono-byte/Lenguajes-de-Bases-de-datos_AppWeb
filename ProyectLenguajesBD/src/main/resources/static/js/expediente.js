const selectorPaciente = document.getElementById('selectorPaciente');

if (selectorPaciente) {
    selectorPaciente.addEventListener('change', function () {
        if (this.value) window.location.href = `/expediente?pacienteId=${this.value}`;
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const selectorPaciente = document.getElementById('selectorPaciente');

    if (selectorPaciente) {
        selectorPaciente.addEventListener('change', () => {
            if (selectorPaciente.value) {
                window.location.href = `/expediente?pacienteId=${selectorPaciente.value}`;
            }
        });
    }
});