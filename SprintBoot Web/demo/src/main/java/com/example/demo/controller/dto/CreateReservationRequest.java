package com.example.demo.controller.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {

    private Long clientId;
    private Long roomTypeId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadPersonas;
}
