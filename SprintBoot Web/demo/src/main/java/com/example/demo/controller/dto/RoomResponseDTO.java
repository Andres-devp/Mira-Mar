package com.example.demo.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponseDTO {
    
    private Long id;
    private String nombre;
    private Long roomTypeId;
    private String roomTypeName;
    private Integer capacidad;
    private Double precioNoche;
}
