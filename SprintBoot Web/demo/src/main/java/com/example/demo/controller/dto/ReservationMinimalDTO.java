package com.example.demo.controller.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationMinimalDTO {
    
    private Long id;
    private String clientNombre;
    private Integer cantidadPersonas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private Long roomId;
    private String roomNombre;
}
