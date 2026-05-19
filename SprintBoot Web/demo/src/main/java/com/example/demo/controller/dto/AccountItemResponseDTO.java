package com.example.demo.controller.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountItemResponseDTO {
    
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private LocalDateTime createdAt;
    private Boolean eliminado;
    
    // Service info - minimal
    private Long hotelServiceId;
    private String hotelServiceNombre;
    private Double hotelServicePrecio;
}
