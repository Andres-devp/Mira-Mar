package com.example.demo.controller;

    import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.service.ClientService;
import com.example.demo.service.HotelServiceService;
import com.example.demo.service.OperatorService;
import com.example.demo.service.ReservationService;
import com.example.demo.service.RoomService;
import com.example.demo.service.RoomTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Administración", description = "Panel de administración")
@PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
