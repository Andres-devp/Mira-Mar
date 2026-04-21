package com.example.demo.service;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservas();

    Reservation getReservaById(Long id);

    Reservation createReserva(CreateReservationRequest request);

    Reservation saveReserva(Reservation reserva);

    void deleteReserva(Long id);
}
