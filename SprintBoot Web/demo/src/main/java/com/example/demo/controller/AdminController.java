package com.example.demo.controller;

import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Administración", description = "Panel de administración")
public class AdminController {

    @Autowired
    private ClientService clienteService;

    @Autowired
    private HotelServiceService servicioService;

    @Autowired
    private RoomService habitacionService;

    @Autowired
    private RoomTypeService tipoHabitacionService;

    @Autowired
    private ReservationService reservaService;

    @Autowired
    private OperatorService operadorService;

    @GetMapping({"/stats", ""})
    @Operation(summary = "Obtener estadísticas del dashboard de administración")
    public Map<String, Object> adminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClientes", clienteService.getAllClientes().size());
        stats.put("totalServicios", servicioService.getAllServicios().size());
        stats.put("totalHabitaciones", habitacionService.getAllHabitaciones().size());
        stats.put("totalTiposHabitacion", tipoHabitacionService.getAllTipos().size());
        stats.put("totalReservas", reservaService.getAllReservas().size());
        stats.put("totalOperadores", operadorService.getAllOperadores().size());
        return stats;
    }
}
