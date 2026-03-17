# Mira Mar Caribbean Luxury 🌊

**Mira Mar** es una plataforma integral de gestión para un resort boutique de lujo. El sistema no solo ofrece una interfaz elegante para los huéspedes, sino que incluye un robusto panel administrativo para la gestión de inventario, servicios y control de usuarios.

<img width="1884" height="910" alt="image" src="https://github.com/user-attachments/assets/6869f1b0-c9a2-4c45-94da-b6f5ffcdb256" />
<img width="1599" height="758" alt="image" src="https://github.com/user-attachments/assets/a73eec51-cd50-47d9-aa20-e4d76d176bc9" />
<img width="1599" height="758" alt="image" src="https://github.com/user-attachments/assets/8d41863c-77da-41ea-81c3-9f1605c2576a" />
<img width="1599" height="759" alt="image" src="https://github.com/user-attachments/assets/9d8fc3b7-b63a-4db5-86dc-eb9e6d418c0e" />
<img width="1598" height="762" alt="image" src="https://github.com/user-attachments/assets/467168b1-5e1a-4d52-862b-751f4767943a" />
<img width="1599" height="758" alt="image" src="https://github.com/user-attachments/assets/cee6fdd4-bf57-4208-af9c-9ca1dc80b1cf" />
<img width="1599" height="763" alt="image" src="https://github.com/user-attachments/assets/d0ca4f5f-bfcd-41f9-90e0-e96162b5fc61" />
<img width="1599" height="756" alt="image" src="https://github.com/user-attachments/assets/09478ad1-72f6-48a5-9f5a-abf8fce2ef3d" />
<img width="1599" height="761" alt="image" src="https://github.com/user-attachments/assets/3a9a8570-dc48-48f9-af48-d640fbfae426" />
<img width="1599" height="757" alt="image" src="https://github.com/user-attachments/assets/8548b774-c005-4222-bacb-b57c49f169c3" />
<img width="1599" height="764" alt="image" src="https://github.com/user-attachments/assets/be7bce4d-3aa5-464e-ac4e-da625c6b8296" />
<img width="1599" height="761" alt="image" src="https://github.com/user-attachments/assets/3bc3fa07-8edb-45e3-9af0-7cb0ed33091a" />
<img width="1599" height="759" alt="image" src="https://github.com/user-attachments/assets/dec5b6f6-0de9-4f6a-9b91-06de59389486" />
<img width="1599" height="759" alt="image" src="https://github.com/user-attachments/assets/1fd01a77-0496-4d65-84cf-07ba9aa3613b" />





![Estado del Proyecto](https://img.shields.io/badge/Estado-En%20Desarrollo-green)
![Java](https://img.shields.io/badge/Backend-Java%2017-orange)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-brightgreen)

## 📋 Módulos del Sistema

### 🔐 Seguridad y Acceso
- **Autenticación:** Sistema de Login personalizado con validación de credenciales.
- **Registro:** Creación de cuentas para nuevos clientes con interfaz moderna (Tailwind CSS).
- **Roles y Permisos:** Diferenciación clara entre **Administradores** (gestión total) y **Clientes** (reservas).

### 🏨 Gestión de Suites (CRUD)
- Visualización de catálogo con precios por noche y capacidad.
- Administración de habitaciones: creación, edición y eliminación desde tablas dinámicas.
- Detalle de habitación con galería de imágenes y resumen de costos.

### 🍽️ Experiencias y Servicios
- Catálogo de servicios adicionales (Spa, Tours, Restaurante).
- Sistema de reserva de servicios con selección de fecha, hora y número de personas.

### 👥 Administración de Usuarios
- Panel de control para gestionar perfiles de usuarios.
- Visualización detallada de información de contacto y roles asignados.

## 🛠️ Stack Tecnológico

* **Backend:** Java 17 + Spring Boot.
* **Frontend:** * **Thymeleaf:** Motor de plantillas dinámicas.
    * **Estilos:** CSS3 nativo, **Tailwind CSS** (Registro) y **Bootstrap 5** (Login).
    * **JS:** Lógica interactiva y manipulacion del DOM.
* **Iconografía:** [Phosphor Icons](https://phosphoricons.com/) & FontAwesome.
* **Base de Datos:** H2 / MySQL (vía Spring Data JPA).

## 🗄️ Arquitectura de Datos
- El siguiente diagrama entidad–relación representa la estructura de la base de datos utilizada en el sistema **Mira Mar Caribbean Luxury**, mostrando las principales entidades como usuarios, reservas, habitaciones, servicios, cuentas y pagos.

### Modelo Entidad–Relación
![alt text](Documents/DiagramaEntidad-Relación.jpg)

### Diagrama De Clases
![alt text](Documents/DiagramaDeClases.jpg)


## 📂 Estructura del Proyecto

Basado en la arquitectura MVC (Modelo-Vista-Controlador):

```text
src/main/java/com/miramar/
├── controller/       # Manejo de rutas (Rooms, Services, Auth, Users)
├── model/            # Entidades de base de datos
├── repository/       # Interfaces de JPA para persistencia
└── service/          # Lógica de negocio

src/main/resources/
├── templates/        # Vistas .html (Thymeleaf)
│   ├── fragments/    # Navbar y Footer reutilizables
│   ├── rooms/        # Gestión de suites
│   ├── HotelServices/# Gestión de experiencias
│   └── Usuarios/      # Gestión de perfiles
└── static/           # Recursos (CSS, JS, Imágenes)
   git clone [https://github.com/Andres-devp/Mira-Mar.git](https://github.com/Andres-devp/Mira-Mar.git)
