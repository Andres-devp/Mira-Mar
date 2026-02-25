document.addEventListener("DOMContentLoaded", () => {
  // --- Navbar scroll ---
  const navbar = document.getElementById("navbar");
  const logoImg = document.getElementById("nav-logo");
  const logoDefault = logoImg ? logoImg.dataset.default || logoImg.src : null;
  const logoScrolled = logoImg ? logoImg.dataset.scrolled || logoDefault : null;
  window.addEventListener("scroll", () => {
    if (window.scrollY > 50) {
      navbar.classList.add("scrolled");
      if (logoImg && logoScrolled) logoImg.src = logoScrolled;
    } else {
      navbar.classList.remove("scrolled");
      if (logoImg && logoDefault) logoImg.src = logoDefault;
    }
  });

  // --- Mobile menu (con clase) ---
  const hamburger = document.querySelector(".hamburger");
  const navLinks = document.querySelector(".nav-links");

  const closeMenu = () => {
    navbar.classList.remove("nav-open");
  };

  if (hamburger) {
    hamburger.addEventListener("click", () => {
      navbar.classList.toggle("nav-open");
    });
  }

  // Cerrar al hacer click en un link
  if (navLinks) {
    navLinks.addEventListener("click", (e) => {
      if (e.target.tagName === "A") closeMenu();
    });
  }


  window.addEventListener("resize", () => {
    if (window.innerWidth > 768) closeMenu();
  });
});