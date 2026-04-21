package com.example.demo.service;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Client;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final String ESTADO_POR_DEFECTO = "PENDING";

    @Autowired
    private ReservationRepository reservaRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservas() {
        return reservaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservaById(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontro reserva con ID: " + id, id));
    }

    @Override
    public Reservation createReserva(CreateReservationRequest request) {
        if (request == null) {
            throw new IllegalStateException("La solicitud de reserva es obligatoria");
        }

        if (request.getClientId() == null) {
            throw new IllegalStateException("El clientId es obligatorio");
        }
        if (request.getRoomTypeId() == null) {
            throw new IllegalStateException("El roomTypeId es obligatorio");
        }
        if (request.getFechaInicio() == null || request.getFechaFin() == null) {
            throw new IllegalStateException("Las fechas de inicio y fin son obligatorias");
        }
        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            throw new IllegalStateException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (request.getCantidadPersonas() == null || request.getCantidadPersonas() <= 0) {
            throw new IllegalStateException("La cantidad de personas debe ser mayor que cero");
        }

        Client client = clientRepository.findById(request.getClientId())
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + request.getClientId(), request.getClientId()));

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + request.getRoomTypeId(), request.getRoomTypeId()));

        if (request.getCantidadPersonas() > roomType.getCapacidad()) {
            throw new IllegalStateException("La cantidad de personas excede la capacidad del tipo de habitación seleccionado");
        }

        List<Room> rooms = roomRepository.findByTipoHabitacionIdOrderByIdAsc(roomType.getId());
        if (rooms.isEmpty()) {
            throw new IllegalStateException("No hay habitaciones registradas para el tipo seleccionado");
        }

        Room roomDisponible = null;
        for (Room room : rooms) {
            boolean tieneCruces = !reservaRepository
                .findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                    room.getId(),
                    request.getFechaFin(),
                    request.getFechaInicio(),
                    "CANCELED"
                )
                .isEmpty();

            if (!tieneCruces) {
                roomDisponible = room;
                break;
            }
        }

        if (roomDisponible == null) {
            throw new IllegalStateException("No hay habitaciones disponibles para ese tipo en las fechas seleccionadas");
        }

        Reservation reserva = Reservation.builder()
            .fechaInicio(request.getFechaInicio())
            .fechaFin(request.getFechaFin())
            .cantidadPersonas(request.getCantidadPersonas())
            .estado(ESTADO_POR_DEFECTO)
            .createdAt(LocalDateTime.now())
            .canceledAt(null)
            .client(client)
            .room(roomDisponible)
            .build();

        return reservaRepository.save(reserva);
    }

    @Override
    public Reservation saveReserva(Reservation reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void deleteReserva(Long id) {
        Reservation reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontro reserva con ID: " + id, id));
        reservaRepository.delete(reserva);
    }
}
