document.addEventListener('DOMContentLoaded', () => {
    const hamburger = document.getElementById('hamburger');
    const navLinks = document.getElementById('nav-links');

    if (hamburger && navLinks) {
        hamburger.addEventListener('click', () => {
            navLinks.classList.toggle('show');
        });
    }

    const servicesInfo = {
        restaurante: 'Disfruta desayunos, almuerzos y cenas con menú local e internacional frente al mar.',
        spa: 'Incluye masajes relajantes, hidroterapia y tratamientos faciales con productos naturales.',
        tours: 'Salidas diarias con guía bilingüe, equipo de seguridad y paradas para fotos.',
        transporte: 'Servicio puerta a puerta con horarios coordinados con tus vuelos.',
        pool: 'Abierto todo el día con mixología tropical, mocktails y tapas ligeras.',
        snorkel: 'Actividades para principiantes y expertos con instructores certificados.',
        coworking: 'Espacio climatizado, café ilimitado y cabinas para videollamadas.',
        gimnasio: 'Maquinaria funcional, área de estiramiento y asesoría básica de entrenamiento.'
    };

    const modal = document.getElementById('service-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalText = document.getElementById('modal-text');
    const modalClose = document.getElementById('modal-close');

    document.querySelectorAll('.details-btn').forEach((btn) => {
        btn.addEventListener('click', () => {
            const cardTitle = btn.closest('.service-card')?.querySelector('h3')?.textContent || 'Servicio';
            const key = btn.dataset.service;
            modalTitle.textContent = cardTitle;
            modalText.textContent = servicesInfo[key] || 'Pronto agregaremos más detalles de este servicio.';
            modal.classList.add('open');
            modal.setAttribute('aria-hidden', 'false');
        });
    });

    if (modalClose) {
        modalClose.addEventListener('click', () => {
            modal.classList.remove('open');
            modal.setAttribute('aria-hidden', 'true');
        });
    }

    modal?.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.classList.remove('open');
            modal.setAttribute('aria-hidden', 'true');
        }
    });

    const reservaForm = document.getElementById('reserva-form');
    const reservaMsg = document.getElementById('reserva-msg');

    reservaForm?.addEventListener('submit', (event) => {
        event.preventDefault();
        reservaMsg.className = 'form-msg';

        const entrada = document.getElementById('entrada').value;
        const salida = document.getElementById('salida').value;
        const huespedes = Number(document.getElementById('huespedes').value);

        if (!entrada || !salida) {
            reservaMsg.textContent = 'Por favor selecciona fecha de entrada y salida.';
            reservaMsg.classList.add('error');
            return;
        }

        if (new Date(salida) <= new Date(entrada)) {
            reservaMsg.textContent = 'La fecha de salida debe ser posterior a la de entrada.';
            reservaMsg.classList.add('error');
            return;
        }

        if (huespedes <= 0 || Number.isNaN(huespedes)) {
            reservaMsg.textContent = 'Indica un número válido de huéspedes.';
            reservaMsg.classList.add('error');
            return;
        }

        reservaMsg.textContent = '¡Excelente! Recibimos tu solicitud y pronto te contactaremos con opciones disponibles.';
        reservaMsg.classList.add('ok');
        reservaForm.reset();
    });

    const contactoForm = document.getElementById('contacto-form');
    const contactoMsg = document.getElementById('contacto-msg');

    contactoForm?.addEventListener('submit', (event) => {
        event.preventDefault();
        contactoMsg.className = 'form-msg';

        const nombre = document.getElementById('nombre').value.trim();
        const email = document.getElementById('email').value.trim();
        const mensaje = document.getElementById('mensaje').value.trim();
        const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

        if (!nombre || !email || !mensaje) {
            contactoMsg.textContent = 'Completa todos los campos del formulario.';
            contactoMsg.classList.add('error');
            return;
        }

        if (!emailValido) {
            contactoMsg.textContent = 'Por favor ingresa un email válido.';
            contactoMsg.classList.add('error');
            return;
        }

        contactoMsg.textContent = '¡Gracias por escribirnos! Te responderemos lo antes posible.';
        contactoMsg.classList.add('ok');
        contactoForm.reset();
    });
});
