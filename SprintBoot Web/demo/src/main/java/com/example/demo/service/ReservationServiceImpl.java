package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.Account;
import com.example.demo.entities.AccountItem;
import com.example.demo.entities.Client;
import com.example.demo.entities.HotelService;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AccountItemRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelServiceRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;

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

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountItemRepository accountItemRepository;

    @Autowired
    private HotelServiceRepository hotelServiceRepository;

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
    public Reservation createReserva(Long sessionUserId, CreateReservationRequest request) {
        if (request == null) {
            throw new IllegalStateException("La solicitud de reserva es obligatoria");
        }

        Long clientId = resolveClientId(sessionUserId, request.getClientId());
        if (clientId == null) {
            throw new IllegalStateException("No se pudo identificar el cliente de la sesión");
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

        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + clientId, clientId));

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

    private Long resolveClientId(Long sessionUserId, Long requestClientId) {
        if (sessionUserId != null) {
            return sessionUserId;
        }
        return requestClientId;
    }

    @Override
    public Reservation saveReserva(Reservation reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public Reservation updateEstado(Long id, String nuevoEstado) {
        Reservation reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró reserva con ID: " + id, id));

        String estadoActual = reserva.getEstado();

        switch (nuevoEstado) {
            case "ACTIVE":
                if (!"PENDING".equals(estadoActual)) {
                    throw new IllegalStateException("Solo se puede activar una reserva en estado PENDING");
                }
                break;
            case "CANCELED":
                if (!"PENDING".equals(estadoActual)) {
                    throw new IllegalStateException("Solo se puede cancelar una reserva en estado PENDING");
                }
                reserva.setCanceledAt(LocalDateTime.now());
                break;
            case "INACTIVE":
                if (!"ACTIVE".equals(estadoActual)) {
                    throw new IllegalStateException("Solo se puede inactivar una reserva en estado ACTIVE");
                }
                accountRepository.findByReservationId(id).ifPresent(cuenta -> {
                    if ("OPEN".equals(cuenta.getEstado())) {
                        throw new IllegalStateException("No se puede inactivar la reserva: la cuenta está OPEN. Realice el pago primero.");
                    }
                });
                break;
            default:
                throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }

        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void deleteReserva(Long id) {
        Reservation reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontro reserva con ID: " + id, id));
        reservaRepository.delete(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountItem> getItemsByReservacion(Long reservationId) {
        return accountRepository.findByReservationId(reservationId)
            .map(account -> accountItemRepository.findByAccountIdAndEliminadoFalse(account.getId()))
            .orElse(List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountItem> getPaidItemsByReservacion(Long reservationId) {
        return accountRepository.findByReservationId(reservationId)
            .map(account -> accountItemRepository.findByAccountIdAndEliminadoTrue(account.getId()))
            .orElse(List.of());
    }

    private void validateEdicionPermitida(Reservation reserva) {
        String estado = reserva.getEstado();
        if (!"PENDING".equals(estado) && !"ACTIVE".equals(estado)) {
            throw new IllegalStateException("Solo se pueden modificar servicios cuando la reserva está en estado PENDING o ACTIVE");
        }

        Account cuenta = accountRepository.findByReservationId(reserva.getId()).orElse(null);
        if (cuenta != null && "CLOSED".equals(cuenta.getEstado())) {
            throw new IllegalStateException("No se pueden modificar servicios cuando la cuenta está CLOSED");
        }
    }

    @Override
    public AccountItem addItemToReservacion(Long reservationId, Long hotelServiceId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        Reservation reservation = reservaRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("No se encontró reserva con ID: " + reservationId, reservationId));

        validateEdicionPermitida(reservation);

        HotelService servicio = hotelServiceRepository.findById(hotelServiceId)
            .orElseThrow(() -> new NotFoundException("No se encontró servicio con ID: " + hotelServiceId, hotelServiceId));

        Account cuenta = accountRepository.findByReservationId(reservationId).orElseGet(() -> {
            Account nueva = Account.builder()
                .saldo(0.0)
                .estado("OPEN")
                .createdAt(LocalDateTime.now()) 
                .reservation(reservation)
                .build();
            return accountRepository.save(nueva);
        });

        double precioUnitario = servicio.getPrice() != null ? servicio.getPrice() : 0.0;

        AccountItem item = AccountItem.builder()
            .cantidad(cantidad)
            .precioUnitario(precioUnitario)
            .subtotal(precioUnitario * cantidad)
            .createdAt(LocalDateTime.now())
            .eliminado(false)
            .account(cuenta)
            .hotelService(servicio)
            .build();

        return accountItemRepository.save(item);
    }

    @Override
    public AccountItem updateItemCantidad(Long itemId, Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        AccountItem item = accountItemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("No se encontró item con ID: " + itemId, itemId));

        validateEdicionPermitida(item.getAccount().getReservation());

        item.setCantidad(nuevaCantidad);
        item.setSubtotal(item.getPrecioUnitario() * nuevaCantidad);

        return accountItemRepository.save(item);
    }

    @Override
    public void removeItem(Long itemId) {
        AccountItem item = accountItemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("No se encontró item con ID: " + itemId, itemId));

        validateEdicionPermitida(item.getAccount().getReservation());

        item.setEliminado(true);
        accountItemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> getAccountByReservacion(Long reservationId) {
        return accountRepository.findByReservationId(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservacionesByCliente(Long clientId) {
        return reservaRepository.findByClientId(clientId);
    }

    @Override
    public Reservation updateReservacion(Long id, Long roomTypeId, LocalDate fechaInicio, LocalDate fechaFin, Integer cantidadPersonas) {
        Reservation reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró reserva con ID: " + id, id));

        if (!"PENDING".equals(reserva.getEstado())) {
            throw new IllegalStateException("Solo se pueden editar reservas en estado PENDING");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        if (cantidadPersonas == null || cantidadPersonas <= 0) {
            throw new IllegalArgumentException("La cantidad de personas debe ser mayor que cero");
        }

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new NotFoundException("No se encontró tipo de habitación con ID: " + roomTypeId, roomTypeId));

        if (cantidadPersonas > roomType.getCapacidad()) {
            throw new IllegalArgumentException("La cantidad de personas excede la capacidad del tipo de habitación seleccionado");
        }

        List<Room> rooms = roomRepository.findByTipoHabitacionIdOrderByIdAsc(roomType.getId());
        if (rooms.isEmpty()) {
            throw new IllegalStateException("No hay habitaciones registradas para el tipo seleccionado");
        }

        Room roomDisponible = null;
        for (Room room : rooms) {
            List<Reservation> cruces = reservaRepository
                .findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                    room.getId(),
                    fechaFin,
                    fechaInicio,
                    "CANCELED"
                );
            boolean tieneCruces = cruces.stream().anyMatch(r -> !r.getId().equals(id));
            if (!tieneCruces) {
                roomDisponible = room;
                break;
            }
        }

        if (roomDisponible == null) {
            throw new IllegalStateException("No hay habitaciones disponibles para ese tipo en las fechas seleccionadas");
        }

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setCantidadPersonas(cantidadPersonas);
        reserva.setRoom(roomDisponible);

        return reservaRepository.save(reserva);
    }

    @Override
    public Account payAccount(Long reservationId) {
        reservaRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("No se encontró reserva con ID: " + reservationId, reservationId));

        Account cuenta = accountRepository.findByReservationId(reservationId)
            .orElseThrow(() -> new IllegalStateException("No hay cuenta asociada a esta reserva"));

        if (!"OPEN".equals(cuenta.getEstado())) {
            throw new IllegalStateException("La cuenta ya está CLOSED");
        }

        List<AccountItem> items = accountItemRepository.findByAccountId(cuenta.getId());
        if (!items.isEmpty()) {
            items.forEach(item -> item.setEliminado(true));
            accountItemRepository.saveAll(items);
        }

        cuenta.setEstado("CLOSED");
        return accountRepository.save(cuenta);
    }
}
