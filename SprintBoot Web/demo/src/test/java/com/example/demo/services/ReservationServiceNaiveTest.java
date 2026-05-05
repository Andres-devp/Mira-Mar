package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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
import com.example.demo.service.ReservationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceNaiveTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountItemRepository accountItemRepository;

    @Autowired
    private HotelServiceRepository hotelServiceRepository;

    private Client crearCliente(String usuario, String email) {
        return clientRepository.save(Client.builder()
                .nombre("Cliente " + usuario)
                .usuario(usuario)
                .contrasena("123")
                .email(email)
                .telefono("3000000000")
                .build());
    }

    private RoomType crearRoomType(String codigo, int capacidad, double precio) {
        return roomTypeRepository.save(RoomType.builder()
                .codigo(codigo)
                .nombre("Tipo " + codigo)
                .descripcion("Desc " + codigo)
                .urlImagen("/img.jpg")
                .precioNoche(precio)
                .capacidad(capacidad)
                .build());
    }

    private Room crearRoom(String nombre, RoomType tipo) {
        return roomRepository.save(Room.builder()
                .nombre(nombre)
                .tipoHabitacion(tipo)
                .build());
    }

    private Reservation crearReserva(Client client, Room room, LocalDate inicio, LocalDate fin, int personas, String estado) {
        return reservationRepository.save(Reservation.builder()
                .fechaInicio(inicio)
                .fechaFin(fin)
                .cantidadPersonas(personas)
                .estado(estado)
                .createdAt(LocalDateTime.now())
                .canceledAt(null)
                .client(client)
                .room(room)
                .build());
    }

    private Account crearCuenta(Reservation reserva, String estado) {
        return accountRepository.save(Account.builder()
                .saldo(0.0)
                .estado(estado)
                .reservation(reserva)
                .build());
    }

    private HotelService crearServicio(String nombre, double price) {
        return hotelServiceRepository.save(HotelService.builder()
                .nombre(nombre)
                .descripcion("Desc " + nombre)
                .imageUrl("/img.jpg")
                .price(price)
                .build());
    }

    @Test
    public void ReservationService_getAllReservas_NotEmptyList() {
        //arrange
        Client client = crearCliente("ana", "ana@test.com");
        RoomType tipo = crearRoomType("SGL1", 2, 100.0);
        Room room = crearRoom("Hab 1", tipo);
        crearReserva(client, room, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");
        crearReserva(client, room, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 5), 2, "ACTIVE");

        //act
        List<Reservation> resultado = reservationService.getAllReservas();

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
    }

    @Test
    public void ReservationService_getReservaById_Reservation() {
        //arrange
        Client client = crearCliente("juan", "juan@test.com");
        RoomType tipo = crearRoomType("SGL2", 2, 100.0);
        Room room = crearRoom("Hab 2", tipo);
        Reservation guardada = crearReserva(client, room, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act
        Reservation resultado = reservationService.getReservaById(guardada.getId());

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(guardada.getId());
        assertThat(resultado.getEstado()).isEqualTo("PENDING");
    }

    @Test
    public void ReservationService_getReservaById_NotFoundException() {
        //arrange
        Long idInexistente = 9999L;

        //act & assert
        assertThrows(NotFoundException.class, () -> reservationService.getReservaById(idInexistente));
    }

    @Test
    public void ReservationService_createReserva_Reservation() {
        //arrange
        Client client = crearCliente("maria", "maria@test.com");
        RoomType tipo = crearRoomType("DBL1", 4, 120.0);
        crearRoom("Hab 10", tipo);

        CreateReservationRequest request = new CreateReservationRequest(
                client.getId(), tipo.getId(),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 5),
                2);

        //act
        Reservation resultado = reservationService.createReserva(null, request);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("PENDING");
        assertThat(resultado.getCantidadPersonas()).isEqualTo(2);
        assertThat(reservationRepository.findById(resultado.getId())).isPresent();
    }

    @Test
    public void ReservationService_createReserva_IllegalStateExceptionWhenCapacidadExcedida() {
        //arrange
        Client client = crearCliente("pedro", "pedro@test.com");
        RoomType tipo = crearRoomType("SGL3", 1, 80.0);
        crearRoom("Hab 11", tipo);

        CreateReservationRequest request = new CreateReservationRequest(
                client.getId(), tipo.getId(),
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 5),
                5);

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReserva(null, request));
    }

    @Test
    public void ReservationService_createReserva_IllegalStateExceptionWhenNoRoomsAvailable() {
        //arrange
        Client client = crearCliente("luis", "luis@test.com");
        RoomType tipo = crearRoomType("DBL2", 4, 120.0);
        Room room = crearRoom("Hab 20", tipo);
        crearReserva(client, room,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 10),
                2, "ACTIVE");

        CreateReservationRequest request = new CreateReservationRequest(
                client.getId(), tipo.getId(),
                LocalDate.of(2026, 8, 3),
                LocalDate.of(2026, 8, 7),
                2);

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReserva(null, request));
    }

    @Test
    public void ReservationService_createReserva_IllegalStateExceptionWhenFechasInvalidas() {
        //arrange
        Client client = crearCliente("eva", "eva@test.com");
        RoomType tipo = crearRoomType("DBL3", 4, 120.0);
        crearRoom("Hab 21", tipo);

        CreateReservationRequest request = new CreateReservationRequest(
                client.getId(), tipo.getId(),
                LocalDate.of(2026, 8, 5),
                LocalDate.of(2026, 8, 1),
                2);

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReserva(null, request));
    }

    @Test
    public void ReservationService_saveReserva_Reservation() {
        //arrange
        Client client = crearCliente("sofia", "sofia@test.com");
        RoomType tipo = crearRoomType("SGL4", 2, 100.0);
        Room room = crearRoom("Hab 30", tipo);
        Reservation reserva = Reservation.builder()
                .fechaInicio(LocalDate.of(2026, 8, 1))
                .fechaFin(LocalDate.of(2026, 8, 5))
                .cantidadPersonas(2)
                .estado("PENDING")
                .createdAt(LocalDateTime.now())
                .client(client)
                .room(room)
                .build();

        //act
        Reservation resultado = reservationService.saveReserva(reserva);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(reservationRepository.findById(resultado.getId())).isPresent();
    }

    @Test
    public void ReservationService_updateEstado_ActiveReservation() {
        //arrange
        Client client = crearCliente("paula", "paula@test.com");
        RoomType tipo = crearRoomType("SGL5", 2, 100.0);
        Room room = crearRoom("Hab 40", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act
        Reservation resultado = reservationService.updateEstado(reserva.getId(), "ACTIVE");

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("ACTIVE");
        assertThat(reservationRepository.findById(reserva.getId()).get().getEstado()).isEqualTo("ACTIVE");
    }

    @Test
    public void ReservationService_updateEstado_CanceledReservation() {
        //arrange
        Client client = crearCliente("david", "david@test.com");
        RoomType tipo = crearRoomType("SGL6", 2, 100.0);
        Room room = crearRoom("Hab 41", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act
        Reservation resultado = reservationService.updateEstado(reserva.getId(), "CANCELED");

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("CANCELED");
        assertThat(resultado.getCanceledAt()).isNotNull();
    }

    @Test
    public void ReservationService_updateEstado_IllegalStateExceptionWhenNotPending() {
        //arrange
        Client client = crearCliente("mateo", "mateo@test.com");
        RoomType tipo = crearRoomType("SGL7", 2, 100.0);
        Room room = crearRoom("Hab 42", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.updateEstado(reserva.getId(), "ACTIVE"));
    }

    @Test
    public void ReservationService_updateEstado_IllegalStateExceptionWhenAccountOpen() {
        //arrange
        Client client = crearCliente("rita", "rita@test.com");
        RoomType tipo = crearRoomType("SGL8", 2, 100.0);
        Room room = crearRoom("Hab 43", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        crearCuenta(reserva, "OPEN");

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.updateEstado(reserva.getId(), "INACTIVE"));
    }

    @Test
    public void ReservationService_updateEstado_IllegalArgumentExceptionWhenInvalidState() {
        //arrange
        Client client = crearCliente("hugo", "hugo@test.com");
        RoomType tipo = crearRoomType("SGL9", 2, 100.0);
        Room room = crearRoom("Hab 44", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act & assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.updateEstado(reserva.getId(), "FANTASMA"));
    }

    @Test
    public void ReservationService_deleteReserva_DeletesReservation() {
        //arrange
        Client client = crearCliente("nora", "nora@test.com");
        RoomType tipo = crearRoomType("SGLA", 2, 100.0);
        Room room = crearRoom("Hab 50", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act
        reservationService.deleteReserva(reserva.getId());

        //assert
        assertThat(reservationRepository.findById(reserva.getId())).isEmpty();
    }

    @Test
    public void ReservationService_deleteReserva_NotFoundException() {
        //arrange
        Long idInexistente = 8888L;

        //act & assert
        assertThrows(NotFoundException.class, () -> reservationService.deleteReserva(idInexistente));
    }

    @Test
    public void ReservationService_getItemsByReservacion_NotEmptyList() {
        //arrange
        Client client = crearCliente("iris", "iris@test.com");
        RoomType tipo = crearRoomType("SGLB", 2, 100.0);
        Room room = crearRoom("Hab 60", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Spa", 50.0);
        reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 2);

        //act
        List<AccountItem> resultado = reservationService.getItemsByReservacion(reserva.getId());

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEliminado()).isFalse();
    }

    @Test
    public void ReservationService_getItemsByReservacion_EmptyList() {
        //arrange
        Long reservaSinCuenta = 7777L;

        //act
        List<AccountItem> resultado = reservationService.getItemsByReservacion(reservaSinCuenta);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    public void ReservationService_getPaidItemsByReservacion_NotEmptyList() {
        //arrange
        Client client = crearCliente("tom", "tom@test.com");
        RoomType tipo = crearRoomType("SGLC", 2, 100.0);
        Room room = crearRoom("Hab 61", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Bar", 20.0);
        reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 1);
        reservationService.payAccount(reserva.getId());

        //act
        List<AccountItem> resultado = reservationService.getPaidItemsByReservacion(reserva.getId());

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEliminado()).isTrue();
    }

    @Test
    public void ReservationService_addItemToReservacion_AccountItem() {
        //arrange
        Client client = crearCliente("kim", "kim@test.com");
        RoomType tipo = crearRoomType("SGLD", 2, 100.0);
        Room room = crearRoom("Hab 70", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Spa", 50.0);

        //act
        AccountItem resultado = reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 2);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(2);
        assertThat(resultado.getSubtotal()).isEqualTo(100.0);
        assertThat(resultado.getEliminado()).isFalse();
        assertThat(accountRepository.findByReservationId(reserva.getId())).isPresent();
    }

    @Test
    public void ReservationService_addItemToReservacion_IllegalArgumentExceptionWhenCantidadZero() {
        //arrange
        Client client = crearCliente("leo", "leo@test.com");
        RoomType tipo = crearRoomType("SGLE", 2, 100.0);
        Room room = crearRoom("Hab 71", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Spa", 50.0);

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 0));
    }

    @Test
    public void ReservationService_addItemToReservacion_IllegalStateExceptionWhenCanceled() {
        //arrange
        Client client = crearCliente("max", "max@test.com");
        RoomType tipo = crearRoomType("SGLF", 2, 100.0);
        Room room = crearRoom("Hab 72", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "CANCELED");
        HotelService servicio = crearServicio("Spa", 50.0);

        //act & assert
        assertThrows(IllegalStateException.class,
                () -> reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 2));
    }

    @Test
    public void ReservationService_updateItemCantidad_AccountItem() {
        //arrange
        Client client = crearCliente("zoe", "zoe@test.com");
        RoomType tipo = crearRoomType("SGLG", 2, 100.0);
        Room room = crearRoom("Hab 80", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Spa", 50.0);
        AccountItem item = reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 1);

        //act
        AccountItem resultado = reservationService.updateItemCantidad(item.getId(), 3);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(3);
        assertThat(resultado.getSubtotal()).isEqualTo(150.0);
    }

    @Test
    public void ReservationService_updateItemCantidad_IllegalArgumentExceptionWhenCantidadNull() {
        //arrange
        Long itemId = 1L;

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateItemCantidad(itemId, null));
    }

    @Test
    public void ReservationService_updateItemCantidad_NotFoundException() {
        //arrange
        Long idInexistente = 6666L;

        //act & assert
        assertThrows(NotFoundException.class,
                () -> reservationService.updateItemCantidad(idInexistente, 2));
    }

    @Test
    public void ReservationService_removeItem_DeletedAccountItem() {
        //arrange
        Client client = crearCliente("ivan", "ivan@test.com");
        RoomType tipo = crearRoomType("SGLH", 2, 100.0);
        Room room = crearRoom("Hab 81", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");
        HotelService servicio = crearServicio("Bar", 20.0);
        AccountItem item = reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 1);

        //act
        reservationService.removeItem(item.getId());

        //assert
        AccountItem actualizado = accountItemRepository.findById(item.getId()).orElseThrow();
        assertThat(actualizado.getEliminado()).isTrue();
    }

    @Test
    public void ReservationService_removeItem_NotFoundException() {
        //arrange
        Long idInexistente = 5555L;

        //act & assert
        assertThrows(NotFoundException.class, () -> reservationService.removeItem(idInexistente));
    }

    @Test
    public void ReservationService_getAccountByReservacion_Account() {
        //arrange
        Client client = crearCliente("noa", "noa@test.com");
        RoomType tipo = crearRoomType("SGLI", 2, 100.0);
        Room room = crearRoom("Hab 90", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        crearCuenta(reserva, "OPEN");

        //act
        Optional<Account> resultado = reservationService.getAccountByReservacion(reserva.getId());

        //assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstado()).isEqualTo("OPEN");
    }

    @Test
    public void ReservationService_getAccountByReservacion_EmptyOptional() {
        //arrange
        Long idInexistente = 4444L;

        //act
        Optional<Account> resultado = reservationService.getAccountByReservacion(idInexistente);

        //assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void ReservationService_getReservacionesByCliente_NotEmptyList() {
        //arrange
        Client client = crearCliente("vera", "vera@test.com");
        RoomType tipo = crearRoomType("SGLJ", 2, 100.0);
        Room room = crearRoom("Hab 91", tipo);
        crearReserva(client, room, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");
        crearReserva(client, room, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 5), 2, "ACTIVE");

        //act
        List<Reservation> resultado = reservationService.getReservacionesByCliente(client.getId());

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
    }

    @Test
    public void ReservationService_updateReservacion_Reservation() {
        //arrange
        Client client = crearCliente("uma", "uma@test.com");
        RoomType tipo = crearRoomType("SGLK", 4, 100.0);
        Room room = crearRoom("Hab 100", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        LocalDate nuevoInicio = LocalDate.of(2026, 9, 1);
        LocalDate nuevoFin = LocalDate.of(2026, 9, 7);

        //act
        Reservation resultado = reservationService.updateReservacion(
                reserva.getId(), tipo.getId(), nuevoInicio, nuevoFin, 3);

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getFechaInicio()).isEqualTo(nuevoInicio);
        assertThat(resultado.getFechaFin()).isEqualTo(nuevoFin);
        assertThat(resultado.getCantidadPersonas()).isEqualTo(3);
    }

    @Test
    public void ReservationService_updateReservacion_IllegalStateExceptionWhenNotPending() {
        //arrange
        Client client = crearCliente("ben", "ben@test.com");
        RoomType tipo = crearRoomType("SGLL", 4, 100.0);
        Room room = crearRoom("Hab 101", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");

        //act & assert
        assertThrows(IllegalStateException.class,
                () -> reservationService.updateReservacion(
                        reserva.getId(), tipo.getId(),
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 7),
                        2));
    }

    @Test
    public void ReservationService_updateReservacion_IllegalArgumentExceptionWhenCapacidadExcedida() {
        //arrange
        Client client = crearCliente("cleo", "cleo@test.com");
        RoomType tipo = crearRoomType("SGLM", 2, 100.0);
        Room room = crearRoom("Hab 102", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "PENDING");

        //act & assert
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.updateReservacion(
                        reserva.getId(), tipo.getId(),
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 7),
                        10));
    }

    @Test
    public void ReservationService_payAccount_ClosedAccount() {
        //arrange
        Client client = crearCliente("dora", "dora@test.com");
        RoomType tipo = crearRoomType("SGLN", 2, 100.0);
        Room room = crearRoom("Hab 110", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");
        HotelService servicio = crearServicio("Spa", 50.0);
        reservationService.addItemToReservacion(reserva.getId(), servicio.getId(), 2);

        //act
        Account resultado = reservationService.payAccount(reserva.getId());

        //assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("CLOSED");
        List<AccountItem> items = accountItemRepository.findByAccountId(resultado.getId());
        assertThat(items).allMatch(AccountItem::getEliminado);
    }

    @Test
    public void ReservationService_payAccount_IllegalStateExceptionWhenAlreadyClosed() {
        //arrange
        Client client = crearCliente("emi", "emi@test.com");
        RoomType tipo = crearRoomType("SGLO", 2, 100.0);
        Room room = crearRoom("Hab 111", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "INACTIVE");
        crearCuenta(reserva, "CLOSED");

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.payAccount(reserva.getId()));
    }

    @Test
    public void ReservationService_payAccount_IllegalStateExceptionWhenNoAccount() {
        //arrange
        Client client = crearCliente("fran", "fran@test.com");
        RoomType tipo = crearRoomType("SGLP", 2, 100.0);
        Room room = crearRoom("Hab 112", tipo);
        Reservation reserva = crearReserva(client, room,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 5), 2, "ACTIVE");

        //act & assert
        assertThrows(IllegalStateException.class, () -> reservationService.payAccount(reserva.getId()));
    }
}
