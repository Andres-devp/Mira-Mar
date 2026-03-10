# Mira Mar Caribbean Luxury 🌊

**Mira Mar** es una plataforma web de gestión hotelera y resort boutique de lujo, diseñada para ofrecer una experiencia elegante y fluida tanto para los huéspedes como para los administradores. Ubicado conceptualmente en la Costa del Caribe (Montería), el sistema permite gestionar suites de lujo y servicios exclusivos.

![Estado del Proyecto](https://img.shields.io/badge/Estado-En%20Desarrollo-green)
![Tecnologías](https://img.shields.io/badge/Stack-Spring%20Boot%20%7C%20Thymeleaf%20%7C%20JS-blue)

## ✨ Características Principales

### 🏨 Gestión de Suites
- **Catálogo de Lujo:** Visualización de suites con detalles de capacidad, precio por noche y descripción.
- **Detalle de Habitación:** Vista individualizada con galería y formulario de reserva integrado.
- **Panel Administrativo:** Tabla de gestión para crear, editar y eliminar habitaciones (CRUD completo).

### 🍽️ Servicios del Resort
- **Experiencias:** Catálogo de servicios (Spa, Restaurante, etc.).
- **Reservas de Servicios:** Formulario dinámico para elegir fecha, hora y número de personas.
- **Administración:** Gestión centralizada de la oferta de servicios del hotel.

### 🎨 Experiencia de Usuario (UX/UI)
- **Diseño Premium:** Estética minimalista usando tipografías como *Playfair Display* e *Inter*.
- **Navbar Dinámica:** Navegación inteligente que cambia de estilo al hacer scroll.
- **Totalmente Responsive:** Optimizado para móviles, tablets y escritorio.

## 🛠️ Tecnologías Utilizadas

- **Backend:** Java con **Spring Boot**.
- **Frontend:** - **Thymeleaf:** Motor de plantillas para renderizado dinámico.
  - **CSS3:** Estilos modulares (base, layouts y componentes específicos).
  - **JavaScript:** Manipulación del DOM y lógica de interacción en el cliente.
- **Iconografía:** [Phosphor Icons](https://phosphoricons.com/).
- **Fuentes:** Google Fonts (Playfair Display & Inter).

## 📂 Estructura del Proyecto (Frontend)

El proyecto utiliza una arquitectura de fragmentos para maximizar la reutilización de código:

- `/fragments`: Contiene el `navbar.html` y `footer.html`.
- `/rooms`: Gestión y visualización de habitaciones.
- `/HotelServices`: Gestión y visualización de servicios.
- `/css`: Hojas de estilo organizadas por módulos (habitaciones, servicios, base).
- `/js`: Lógica interactiva global (`script.js`).

## 🚀 Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/Andres-devp/Mira-Mar.git](https://github.com/Andres-devp/Mira-Mar.git)
