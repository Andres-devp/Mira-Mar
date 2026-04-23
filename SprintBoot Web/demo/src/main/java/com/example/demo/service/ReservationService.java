package com.example.demo.service;

import java.util.List;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Reservation;

public interface ReservationService {

    List<Reservation> getAllReservas();

    Reservation getReservaById(Long id);

    Reservation createReserva(Long sessionUserId, CreateReservationRequest request);

    Reservation saveReserva(Reservation reserva);

    void deleteReserva(Long id);
}
