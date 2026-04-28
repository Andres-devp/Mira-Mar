package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.AccountItem;
import com.example.demo.entities.Reservation;

public interface ReservationService {

    List<Reservation> getAllReservas();

    Reservation getReservaById(Long id);

    Reservation createReserva(Long sessionUserId, CreateReservationRequest request);

    Reservation saveReserva(Reservation reserva);

    Reservation updateEstado(Long id, String nuevoEstado);

    void deleteReserva(Long id);

    List<AccountItem> getItemsByReservacion(Long reservationId);

    AccountItem addItemToReservacion(Long reservationId, Long hotelServiceId, Integer cantidad);

    AccountItem updateItemCantidad(Long itemId, Integer nuevaCantidad);

    void removeItem(Long itemId);

    Optional<Account> getAccountByReservacion(Long reservationId);

    Account payAccount(Long reservationId);

    List<Reservation> getReservacionesByCliente(Long clientId);

    Reservation updateReservacion(Long id, Long roomTypeId, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadPersonas);
}
