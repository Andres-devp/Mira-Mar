package com.example.demo.controller.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {
    
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadPersonas;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime canceledAt;
    
    // Client info - minimal
    private Long clientId;
    private String clientNombre;
    private String clientEmail;
    
    // Room info - minimal
    private Long roomId;
    private String roomNombre;
    private Long roomTypeId;
    private String roomTypeName;
}
