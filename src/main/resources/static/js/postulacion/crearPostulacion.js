document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('.needs-validation');
    const steps = document.querySelectorAll('.step');
    const sections = document.querySelectorAll('.section-card');
    const nextButtons = document.querySelectorAll('.btn-next');
    const prevButtons = document.querySelectorAll('.btn-prev');
    const progressBar = document.querySelector('.progress-bar');
    let currentStep = 0;

    // Ocultar todas las secciones excepto la primera
    function initializeSections() {
        sections.forEach((section, index) => {
            if (index === 0) {
                section.classList.add('active');
            } else {
                section.classList.remove('active');
            }
        });
        updateProgress();
    }

    // Actualizar la barra de progreso
    function updateProgress() {
        const progress = ((currentStep + 1) / sections.length) * 100;
        progressBar.style.width = `${progress}%`;
        
        // Actualizar estados de los pasos
        steps.forEach((step, index) => {
            if (index <= currentStep) {
                step.classList.add('active');
            } else {
                step.classList.remove('active');
            }
        });
    }

    // Validar sección actual
    function validateSection(sectionIndex) {
        const inputs = sections[sectionIndex].querySelectorAll('input, select, textarea');
        let isValid = true;

        inputs.forEach(input => {
            if (input.required && !input.value) {
                isValid = false;
                input.classList.add('is-invalid');
            } else {
                input.classList.remove('is-invalid');
            }
        });

        return isValid;
    }

    // Navegación entre secciones
    function goToStep(step) {
        if (step >= 0 && step < sections.length) {
            sections[currentStep].classList.remove('active');
            currentStep = step;
            sections[currentStep].classList.add('active');
            updateProgress();
        }
    }

    // Event listeners para los botones
    nextButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            if (validateSection(currentStep)) {
                goToStep(currentStep + 1);
            }
        });
    });

    prevButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            goToStep(currentStep - 1);
        });
    });

    // Validación del formulario completo
    form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);

    // Cargar la URL de la foto de la startup seleccionada
    const startupSelect = document.getElementById('startup');
    const fotoUrlInput = document.getElementById('fotoUrl');
    
    if (startupSelect && fotoUrlInput) {
        startupSelect.addEventListener('change', function() {
            const selectedOption = startupSelect.options[startupSelect.selectedIndex];
            if (selectedOption && selectedOption.value) {
                // Realizar una petición AJAX para obtener la URL de la foto de la startup
                fetch(`/api/startup/${selectedOption.value}/foto`)
                    .then(response => response.text())
                    .then(fotoUrl => {
                        if (fotoUrl) {
                            fotoUrlInput.value = fotoUrl;
                        }
                    })
                    .catch(error => {
                        console.error('Error al obtener la URL de la foto:', error);
                    });
            }
        });
    }

    // Inicializar
    initializeSections();
});
