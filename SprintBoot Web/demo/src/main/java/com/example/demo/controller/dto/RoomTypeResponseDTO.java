package com.example.demo.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeResponseDTO {
    
    private Long id;
    private String codigo;
    private String nombre;
    private Integer capacidad;
    private String descripcion;
    private String urlImagen;
    private Double precioNoche;
    private Integer cantidadHabitaciones;
}
