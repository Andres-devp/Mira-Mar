package com.example.demo.controller;

import com.example.demo.entities.Room;
import com.example.demo.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Habitaciones", description = "Gestión de habitaciones")
public class RoomController {

    @Autowired
    private RoomService habitacionService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todas las habitaciones")
    public List<Room> listRooms() {
        return habitacionService.getAllHabitaciones();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "Buscar habitación por ID")
    public Room findById(@PathVariable Long id) {
        return habitacionService.getHabitacionById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nueva habitación")
    public Room createRoom(@RequestBody Room room) {
        return habitacionService.saveHabitacion(room);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar habitación existente")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        return habitacionService.saveHabitacion(room);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar habitación por ID")
    public void deleteRoom(@PathVariable Long id) {
        habitacionService.deleteHabitacion(id);
    }
}
