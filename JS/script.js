document.addEventListener('DOMContentLoaded', () => {

  // --- Navbar scroll ---
  const navbar = document.getElementById('navbar');
  window.addEventListener('scroll', () => {
    if (window.scrollY > 50) navbar.classList.add('scrolled');
    else navbar.classList.remove('scrolled');
  });

  // --- Mobile menu (con clase) ---
  const hamburger = document.querySelector('.hamburger');
  const navLinks = document.querySelector('.nav-links');

  const closeMenu = () => {
    navbar.classList.remove('nav-open');
  };

  if (hamburger) {
    hamburger.addEventListener('click', () => {
      navbar.classList.toggle('nav-open');
    });
  }

  // Cerrar al hacer click en un link
  if (navLinks) {
    navLinks.addEventListener('click', (e) => {
      if (e.target.tagName === 'A') closeMenu();
    });
  }

  // ✅ Si agrandan pantalla, se cierra el menú (tu bug)
  window.addEventListener('resize', () => {
    if (window.innerWidth > 768) closeMenu();
  });

});
