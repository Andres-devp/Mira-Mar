# Guía de Prueba - Refactorización H2

## ✅ Cambios Completados

Tu aplicación ha sido refactorizada completamente para usar H2 en lugar de datos quemados. Aquí está el resumen:

## 📦 Estructura Nueva

### Entidades JPA (Con restricciones)
- **Cliente**: Usuario con campos validados (email único, usuario único)
- **TipoHabitacion**: Propietario de relación OneToMany
- **Habitacion**: Referencia ManyToOne a TipoHabitacion

### Capas Implementadas
- **Repository**: `HabitacionRepository`, `TipoHabitacionRepository`, `ClienteRepository`
- **Service**: `HabitacionService`, `TipoHabitacionService`, `ClienteService`
  - Todas con `@Transactional`
  - Desasociaciones correctas en métodos de eliminación
  - `.orElseThrow()` con `NotFoundException`
- **Handler**: `GlobalExceptionHandler` captura excepciones y renderiza `error.html`

### Configuración
- **Base de datos**: H2 en archivo (`./mydatabase`)
- **DDL**: `create-drop` (recrea en cada arranque)
- **Logs**: SQL DEBUG habilitado con parámetros

### Datos Iniciales (DataLoader)
- 5 tipos de habitación
- 10 clientes
- 50 habitaciones (tipos asignados aleatoriamente con seed 42)

## 🚀 Cómo Probar

### 1. Iniciar la aplicación
```bash
mvn spring-boot:run
# or
mvn clean package && java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### 2. Verificar datos cargados
- **H2 Console**: http://localhost:8088/h2
  - JDBC URL: `jdbc:h2:file:./mydatabase`
  - Usuario: `sa`
  - Contraseña: (vacío)

### 3. Probar endpoints
- **Listar habitaciones**: http://localhost:8088/rooms
- **Ver tabla**: http://localhost:8088/rooms/table
- **Crear habitación**: http://localhost:8088/rooms/add
- **Ver detalle**: http://localhost:8088/rooms/1
- **Editar**: http://localhost:8088/rooms/1/edit

### 4. Probar manejo de errores
- Acceder a habitación inexistente: http://localhost:8088/rooms/99999
  - Debe mostrar mensaje de error personalizado en `error.html`

## 📋 Archivos Nuevos Creados

```
src/main/java/com/example/demo/
├── config/
│   └── DataLoader.java (Inicializa datos)
├── entities/
│   ├── Cliente.java (Nueva)
│   ├── Habitacion.java (Nueva con JPA)
│   └── TipoHabitacion.java (Nueva con JPA)
├── exception/
│   └── NotFoundException.java
├── handler/
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── ClienteRepository.java
│   ├── HabitacionRepository.java
│   └── TipoHabitacionRepository.java
└── service/
    ├── ClienteService.java
    ├── HabitacionService.java
    └── TipoHabitacionService.java
```

## 🔧 Archivos Modificados

- `pom.xml` - Agregadas dependencias JPA y H2
- `application.properties` - Configuración H2 y Hibernate
- `RoomController.java` - Actualizado a nuevos servicios
- Vistas Thymeleaf:
  - `error.html` - Muestra `${mensaje}`
  - `room-detail.html` - Usa nuevos campos y relaciones
  - `room-form.html` - Binds correctos con `tipoHabitacion.id`
  - `rooms-list.html` - Muestra tipo desde relación
  - `rooms-table.html` - Tabla actualizada

## ⚙️ Características Implementadas

✅ **Restricciones de Base de Datos**:
- Campos `@Column(nullable = false, unique = true, length = X)`
- IDs `@GeneratedValue(IDENTITY)`

✅ **Relaciones JPA**:
- OneToMany (TipoHabitacion → Habitacion)
- ManyToOne (Habitacion → TipoHabitacion)
- `mappedBy` en lado débil

✅ **Servicios Transaccionales**:
- `@Transactional` en métodos
- Deseralización de relaciones antes de delete
- `orElseThrow()` con excepciones personalizadas

✅ **Manejo de Errores Global**:
- `@ControllerAdvice` con `@ExceptionHandler`
- Pasa mensajes a vistas vía `Model`

✅ **Datos Iniciales**:
- `CommandLineRunner` con `@Component`
- `@Transactional` en DataLoader
- Random seeded para reproducibilidad

## 📝 Notas Importantes

1. **Sin @Data**: Se usó `@Getter`, `@Setter`, `@ToString(exclude = {...})` para evitar loops infinitos
2. **Patrones**: Se incluyó `@Builder` en todas las entidades
3. **Constructores**: Constructores vacíos y completos (excepto ID)
4. **Compilación**: ✅ Sin errores (solo warnings menores de Lombok)

## 🔗 Relaciones

```
TipoHabitacion (1) ----< (N) Habitacion
   - OneToMany con cascade=ALL
   - Limpieza automática en delete (orphanRemoval=true)
   - ManyToOne lazy loading
```

## 🎯 Próximos Pasos Opcionales

1. Crear controladores para TipoHabitacion y Cliente
2. Actualizar vistas para las otras entidades
3. Agregar autenticación con Usuario/Cliente
4. Implementar reservaciones

¡**La refactorización está completa y lista para usar!**
