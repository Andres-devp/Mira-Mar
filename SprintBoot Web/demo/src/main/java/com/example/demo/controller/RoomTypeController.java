package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<RoomType> listTypes() {
        return tipoHabitacionService.getAllTipos();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar tipos por capacidad y/o precio máximo")
    public List<RoomType> filterTypes(
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) Double precioMax) {
        return tipoHabitacionService.filtrarTipos(capacidad, precioMax);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de habitación por ID")
    public RoomType findById(@PathVariable Long id) {
        return tipoHabitacionService.getTipoById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo tipo de habitación")
    public RoomType createType(@RequestBody RoomType tipo) {
        return tipoHabitacionService.saveTipo(tipo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de habitación existente")
    public RoomType updateType(@PathVariable Long id, @RequestBody RoomType tipo) {
        tipo.setId(id);
        return tipoHabitacionService.saveTipo(tipo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de habitación por ID")
    public void deleteType(@PathVariable Long id) {
        tipoHabitacionService.deleteTipo(id);
    }
}
