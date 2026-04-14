package com.example.demo.controller;

import com.example.demo.entities.Operator;
import com.example.demo.service.OperatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operator")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Operadores", description = "Gestión de operadores")
public class OperatorController {

    @Autowired
    private OperatorService operadorService;

    @GetMapping({"/all", ""})
    @Operation(summary = "Listar todos los operadores")
    public List<Operator> listOperators() {
        return operadorService.getAllOperadores();
    }

    @GetMapping("/find/{id}")
    @Operation(summary = "Buscar operador por ID")
    public Operator findById(@PathVariable Long id) {
        return operadorService.getOperadorById(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Crear nuevo operador")
    public Operator createOperator(@RequestBody Operator operador) {
        return operadorService.saveOperador(operador);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar operador existente")
    public Operator updateOperator(@PathVariable Long id, @RequestBody Operator operador) {
        operador.setId(id);
        return operadorService.saveOperador(operador);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar operador por ID")
    public void deleteOperator(@PathVariable Long id) {
        operadorService.deleteOperador(id);
    }
}
