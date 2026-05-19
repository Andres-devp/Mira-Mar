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
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.controller.dto.OperatorResponseDTO;
import com.example.demo.entities.Operator;
import com.example.demo.service.OperatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/operator")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Operadores", description = "Gestión de operadores")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
public class OperatorController {

    @Autowired
    private OperatorService operadorService;

    @GetMapping({"all", ""})
    @Operation(summary = "Listar todos los operadores")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<OperatorResponseDTO>> listOperators() {
        List<OperatorResponseDTO> dtos = operadorService.getAllOperadores()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar operador por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<OperatorResponseDTO> findById(@PathVariable Long id) {
        Operator op = operadorService.getOperadorById(id);
        return ResponseEntity.ok(mapToDTO(op));
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo operador")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorResponseDTO> createOperator(@RequestBody Operator operador) {
        Operator saved = operadorService.saveOperador(operador);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar operador existente")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorResponseDTO> updateOperator(@PathVariable Long id, @RequestBody Operator operador) {
        operador.setId(id);
        Operator updated = operadorService.saveOperador(operador);
        return ResponseEntity.ok(mapToDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar operador por ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        operadorService.deleteOperador(id);
        return ResponseEntity.noContent().build();
    }

    // DTO Mapping
    private OperatorResponseDTO mapToDTO(Operator op) {
        return OperatorResponseDTO.builder()
            .id(op.getId())
            .nombre(op.getNombre())
            .apellido(op.getApellido())
            .usuario(op.getUsuario())
            .email(op.getEmail())
            .cedula(op.getCedula()) 
            .telefono(op.getTelefono())
            .build();
    }
}
