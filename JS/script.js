// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {
    
    // --- Efecto scroll en Navbar ---
    const navbar = document.getElementById('navbar');
    
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    });

    // --- Menú móvil simple ---
    const hamburger = document.querySelector('.hamburger');
    const navLinks = document.querySelector('.nav-links');

    if (hamburger) {
        hamburger.addEventListener('click', () => {
            const isVisible = window.getComputedStyle(navLinks).display !== 'none';
            
            if (isVisible) {
                // Si está visible, lo ocultamos y reseteamos estilos inline
                navLinks.style.display = 'none';
                navLinks.style.position = '';
                navLinks.style.background = '';
            } else {
                // Mostrar menú
                navLinks.style.display = 'flex';
                navLinks.style.flexDirection = 'column';
                navLinks.style.position = 'absolute';
                navLinks.style.top = '70px';
                navLinks.style.left = '0';
                navLinks.style.width = '100%';
                navLinks.style.background = '#304C89'; // Dusk Blue
                navLinks.style.padding = '2rem';
                navLinks.style.textAlign = 'center';
            }
        });
    }
});