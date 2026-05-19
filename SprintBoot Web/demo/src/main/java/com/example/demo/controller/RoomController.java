package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.RoomResponseDTO;
import com.example.demo.entities.Room;
import com.example.demo.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Habitaciones", description = "Gestión de habitaciones")
public class RoomController {

    @Autowired
    private RoomService habitacionService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todas las habitaciones")
    public ResponseEntity<List<RoomResponseDTO>> listRooms() {
        List<RoomResponseDTO> dtos = habitacionService.getAllHabitaciones()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar habitación por ID")
    public ResponseEntity<RoomResponseDTO> findById(@PathVariable Long id) {
        Room room = habitacionService.getHabitacionById(id);
        return ResponseEntity.ok(mapToDTO(room));
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nueva habitación")
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody Room room) {
        Room saved = habitacionService.saveHabitacion(room);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar habitación existente")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        Room updated = habitacionService.saveHabitacion(room);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar habitación por ID")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        habitacionService.deleteHabitacion(id);
        return ResponseEntity.noContent().build();
    }

    // DTO Mapping
    private RoomResponseDTO mapToDTO(Room room) {
        return RoomResponseDTO.builder()
            .id(room.getId())
            .nombre(room.getNombre())
            .roomTypeId(room.getTipoHabitacion().getId())
            .roomTypeName(room.getTipoHabitacion().getNombre())
            .capacidad(room.getTipoHabitacion().getCapacidad())
            .precioNoche(room.getTipoHabitacion().getPrecioNoche())
            .build();
    }
}
