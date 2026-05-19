package com.example.demo.controller.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDTO {
    
    private Long id;
    private String estado;
    private Double total;
    private LocalDateTime createdAt;
    private Long reservationId;
}
