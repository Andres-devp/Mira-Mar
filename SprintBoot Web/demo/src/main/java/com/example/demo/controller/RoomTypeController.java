package com.example.demo.controller;

import java.util.List;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.controller.dto.RoomTypeResponseDTO;
import com.example.demo.entities.RoomType;
import com.example.demo.service.RoomTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/roomtypes")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Tipos de Habitación", description = "Gestión de tipos de habitación")
public class RoomTypeController {

    @Autowired
    private RoomTypeService tipoHabitacionService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todos los tipos de habitación")
    public ResponseEntity<List<RoomTypeResponseDTO>> listTypes() {
        List<RoomTypeResponseDTO> dtos = tipoHabitacionService.getAllTipos()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar tipos por capacidad, precio máximo y disponibilidad por fechas")
    public ResponseEntity<List<RoomTypeResponseDTO>> filterTypes(
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {
        List<RoomTypeResponseDTO> dtos = tipoHabitacionService.filtrarTipos(capacidad, precioMax, fechaInicio, fechaFin)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de habitación por ID")
    public ResponseEntity<RoomTypeResponseDTO> findById(@PathVariable Long id) {
        RoomType tipo = tipoHabitacionService.getTipoById(id);
        return ResponseEntity.ok(mapToDTO(tipo));
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo tipo de habitación")
    public ResponseEntity<RoomTypeResponseDTO> createType(@RequestBody RoomType tipo) {
        RoomType saved = tipoHabitacionService.saveTipo(tipo);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de habitación existente")
    public ResponseEntity<RoomTypeResponseDTO> updateType(@PathVariable Long id, @RequestBody RoomType tipo) {
        tipo.setId(id);
        RoomType updated = tipoHabitacionService.saveTipo(tipo);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de habitación por ID")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        tipoHabitacionService.deleteTipo(id);
        return ResponseEntity.noContent().build();
    }

    // DTO Mapping
    private RoomTypeResponseDTO mapToDTO(RoomType tipo) {
        return RoomTypeResponseDTO.builder()
            .id(tipo.getId())
            .codigo(tipo.getCodigo())
            .nombre(tipo.getNombre())
            .capacidad(tipo.getCapacidad())
            .descripcion(tipo.getDescripcion())
            .precioNoche(tipo.getPrecioNoche())
            .cantidadHabitaciones(tipo.getHabitaciones() != null ? tipo.getHabitaciones().size() : 0)
            .build();
    }
}
