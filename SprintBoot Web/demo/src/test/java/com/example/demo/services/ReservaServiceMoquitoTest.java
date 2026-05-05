package com.example.demo.services;

import com.example.demo.controller.dto.CreateReservationRequest;
import com.example.demo.entities.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceMoquitoTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountItemRepository accountItemRepository;

    @Mock
    private HotelServiceRepository hotelServiceRepository;

    @Test
    void ReservationService_getAllReservas_notEmptyList() {
        // Arrange
        Reservation r1 = Reservation.builder().id(1L).estado("PENDING").build();
        Reservation r2 = Reservation.builder().id(2L).estado("ACTIVE").build();
        when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));

        // Act
        List<Reservation> resultado = reservationService.getAllReservas();

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
    }

    @Test
    void ReservationService_getReservaById_Reservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        Reservation resultado = reservationService.getReservaById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("PENDING");
    }

    @Test
    void ReservationService_getReservaById_NotFoundException() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.getReservaById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void ReservationService_createReserva_Reservation() {
        // Arrange
        Client client = Client.builder().id(1L).nombre("Ana Torres").usuario("ana").contrasena("pass").email("ana@test.com").build();
        RoomType roomType = RoomType.builder().id(10L).capacidad(4).precioNoche(100.0).codigo("SGL").nombre("Simple").build();
        Room room = Room.builder().id(100L).nombre("Hab 101").tipoHabitacion(roomType).build();

        CreateReservationRequest request = new CreateReservationRequest(
                1L, 10L,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 5),
                2
        );

        Reservation reservaGuardada = Reservation.builder()
                .id(200L)
                .estado("PENDING")
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .cantidadPersonas(2)
                .createdAt(LocalDateTime.now())
                .client(client)
                .room(room)
                .build();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));
        when(roomRepository.findByTipoHabitacionIdOrderByIdAsc(10L)).thenReturn(List.of(room));
        when(reservationRepository.findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                anyLong(), any(), any(), anyString()))
                .thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservaGuardada);

        // Act
        Reservation resultado = reservationService.createReserva(null, request);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(200L);
        assertThat(resultado.getEstado()).isEqualTo("PENDING");
        assertThat(resultado.getCantidadPersonas()).isEqualTo(2);
    }

    @Test
    void ReservationService_createReserva_IllegalStateExceptionWhenCapacidadExcedida() {
        // Arrange
        Client client = Client.builder().id(1L).nombre("Juan").usuario("juan").contrasena("pass").email("juan@test.com").build();
        RoomType roomType = RoomType.builder().id(10L).capacidad(1).precioNoche(80.0).codigo("SGL").nombre("Simple").build();

        CreateReservationRequest request = new CreateReservationRequest(
                1L, 10L,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 5),
                5 // excede capacidad maxima de 1
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.createReserva(null, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("capacidad");
    }

    @Test
    void ReservationService_createReserva_IllegalStateExceptionWhenNoRoomsAvailable() {
        // Arrange
        Client client = Client.builder().id(1L).nombre("Maria").usuario("maria").contrasena("pass").email("maria@test.com").build();
        RoomType roomType = RoomType.builder().id(10L).capacidad(4).precioNoche(100.0).codigo("DBL").nombre("Doble").build();
        Room room = Room.builder().id(100L).nombre("Hab 201").tipoHabitacion(roomType).build();
        Reservation conflicto = Reservation.builder().id(5L).estado("ACTIVE").build();

        CreateReservationRequest request = new CreateReservationRequest(
                1L, 10L,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 5),
                2
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));
        when(roomRepository.findByTipoHabitacionIdOrderByIdAsc(10L)).thenReturn(List.of(room));
        when(reservationRepository.findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                anyLong(), any(), any(), anyString()))
                .thenReturn(List.of(conflicto));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.createReserva(null, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("disponibles");
    }

    @Test
    void ReservationService_saveReserva_Reservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.save(reserva)).thenReturn(reserva);

        // Act
        Reservation resultado = reservationService.saveReserva(reserva);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(reservationRepository, times(1)).save(reserva);
    }

    @Test
    void ReservationService_updateEstado_ActiveReservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation resultado = reservationService.updateEstado(1L, "ACTIVE");

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("ACTIVE");
    }

    @Test
    void ReservationService_updateEstado_CanceledReservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation resultado = reservationService.updateEstado(1L, "CANCELED");

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("CANCELED");
        assertThat(resultado.getCanceledAt()).isNotNull();
    }

    @Test
    void ReservationService_updateEstado_InactiveReservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        Account cuentaCerrada = Account.builder().id(1L).estado("CLOSED").reservation(reserva).build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuentaCerrada));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation resultado = reservationService.updateEstado(1L, "INACTIVE");

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("INACTIVE");
    }

    @Test
    void ReservationService_updateEstado_IllegalStateExceptionWhenNotPending() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateEstado(1L, "ACTIVE"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDING");
    }

    @Test
    void ReservationService_updateEstado_IllegalStateExceptionWhenAccountOpen() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        Account cuentaAbierta = Account.builder().id(1L).estado("OPEN").reservation(reserva).build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuentaAbierta));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateEstado(1L, "INACTIVE"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("OPEN");
    }

    @Test
    void ReservationService_updateEstado_IllegalArgumentExceptionWhenInvalidState() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateEstado(1L, "FANTASMA"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FANTASMA");
    }

    @Test
    void ReservationService_deleteReserva_DeletesReservation() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));

        // Act
        reservationService.deleteReserva(1L);

        // Assert
        verify(reservationRepository, times(1)).delete(reserva);
    }

    @Test
    void ReservationService_deleteReserva_NotFoundException() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.deleteReserva(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ReservationService_getItemsByReservacion_notEmptyList() {
        // Arrange
        Account cuenta = Account.builder().id(1L).estado("OPEN").build();
        AccountItem item = AccountItem.builder().id(1L).cantidad(2).eliminado(false).build();
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));
        when(accountItemRepository.findByAccountIdAndEliminadoFalse(1L)).thenReturn(List.of(item));

        // Act
        List<AccountItem> resultado = reservationService.getItemsByReservacion(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEliminado()).isFalse();
    }

    @Test
    void ReservationService_getItemsByReservacion_emptyList() {
        // Arrange
        when(accountRepository.findByReservationId(99L)).thenReturn(Optional.empty());

        // Act
        List<AccountItem> resultado = reservationService.getItemsByReservacion(99L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    void ReservationService_getPaidItemsByReservacion_notEmptyList() {
        // Arrange
        Account cuenta = Account.builder().id(1L).estado("CLOSED").build();
        AccountItem item = AccountItem.builder().id(1L).cantidad(1).eliminado(true).build();
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));
        when(accountItemRepository.findByAccountIdAndEliminadoTrue(1L)).thenReturn(List.of(item));

        // Act
        List<AccountItem> resultado = reservationService.getPaidItemsByReservacion(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEliminado()).isTrue();
    }

    @Test
    void ReservationService_getPaidItemsByReservacion_emptyList() {
        // Arrange
        when(accountRepository.findByReservationId(99L)).thenReturn(Optional.empty());

        // Act
        List<AccountItem> resultado = reservationService.getPaidItemsByReservacion(99L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    void ReservationService_addItemToReservacion_AccountItem() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        HotelService servicio = HotelService.builder().id(5L).nombre("Spa").price(50.0).build();
        Account cuentaNueva = Account.builder().id(10L).saldo(0.0).estado("OPEN").reservation(reserva).build();
        AccountItem itemGuardado = AccountItem.builder()
                .id(1L).cantidad(2).precioUnitario(50.0).subtotal(100.0).eliminado(false)
                .account(cuentaNueva).hotelService(servicio)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.empty());
        when(hotelServiceRepository.findById(5L)).thenReturn(Optional.of(servicio));
        when(accountRepository.save(any(Account.class))).thenReturn(cuentaNueva);
        when(accountItemRepository.save(any(AccountItem.class))).thenReturn(itemGuardado);

        // Act
        AccountItem resultado = reservationService.addItemToReservacion(1L, 5L, 2);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(2);
        assertThat(resultado.getSubtotal()).isEqualTo(100.0);
        assertThat(resultado.getEliminado()).isFalse();
    }

    @Test
    void ReservationService_addItemToReservacion_IllegalArgumentExceptionWhenCantidadZero() {
        // Arrange
        // (validacion falla antes de invocar cualquier repositorio)

        // Act & Assert
        assertThatThrownBy(() -> reservationService.addItemToReservacion(1L, 5L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void ReservationService_addItemToReservacion_IllegalStateExceptionWhenCanceled() {
        // Arrange
        Reservation reservaCancelada = Reservation.builder().id(1L).estado("CANCELED").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservaCancelada));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.addItemToReservacion(1L, 5L, 2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDING o ACTIVE");
    }

    @Test
    void ReservationService_addItemToReservacion_IllegalStateExceptionWhenAccountClosed() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        Account cuentaCerrada = Account.builder().id(1L).estado("CLOSED").reservation(reserva).build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuentaCerrada));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.addItemToReservacion(1L, 5L, 2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CLOSED");
    }

    @Test
    void ReservationService_updateItemCantidad_AccountItem() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        Account cuenta = Account.builder().id(1L).estado("OPEN").reservation(reserva).build();
        AccountItem item = AccountItem.builder()
                .id(1L).cantidad(1).precioUnitario(50.0).subtotal(50.0).eliminado(false)
                .account(cuenta)
                .build();

        when(accountItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));
        when(accountItemRepository.save(any(AccountItem.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        AccountItem resultado = reservationService.updateItemCantidad(1L, 3);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidad()).isEqualTo(3);
        assertThat(resultado.getSubtotal()).isEqualTo(150.0);
    }

    @Test
    void ReservationService_updateItemCantidad_IllegalArgumentExceptionWhenCantidadNull() {
        // Arrange
        // (validacion falla antes de invocar cualquier repositorio)

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateItemCantidad(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad");
    }

    @Test
    void ReservationService_updateItemCantidad_NotFoundException() {
        // Arrange
        when(accountItemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateItemCantidad(99L, 2))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ReservationService_removeItem_DeletedAccountItem() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        Account cuenta = Account.builder().id(1L).estado("OPEN").reservation(reserva).build();
        AccountItem item = AccountItem.builder()
                .id(1L).cantidad(1).eliminado(false)
                .account(cuenta)
                .build();

        when(accountItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));
        when(accountItemRepository.save(any(AccountItem.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        reservationService.removeItem(1L);

        // Assert
        assertThat(item.getEliminado()).isTrue();
        verify(accountItemRepository, times(1)).save(item);
    }

    @Test
    void ReservationService_removeItem_NotFoundException() {
        // Arrange
        when(accountItemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.removeItem(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void ReservationService_getAccountByReservacion_Account() {
        // Arrange
        Account cuenta = Account.builder().id(1L).saldo(0.0).estado("OPEN").build();
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));

        // Act
        Optional<Account> resultado = reservationService.getAccountByReservacion(1L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstado()).isEqualTo("OPEN");
    }

    @Test
    void ReservationService_getAccountByReservacion_emptyOptional() {
        // Arrange
        when(accountRepository.findByReservationId(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Account> resultado = reservationService.getAccountByReservacion(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void ReservationService_getReservacionesByCliente_notEmptyList() {
        // Arrange
        Reservation r1 = Reservation.builder().id(1L).estado("ACTIVE").build();
        Reservation r2 = Reservation.builder().id(2L).estado("PENDING").build();
        when(reservationRepository.findByClientId(1L)).thenReturn(List.of(r1, r2));

        // Act
        List<Reservation> resultado = reservationService.getReservacionesByCliente(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
    }

    @Test
    void ReservationService_updateReservacion_Reservation() {
        // Arrange
        LocalDate inicio = LocalDate.of(2026, 9, 1);
        LocalDate fin = LocalDate.of(2026, 9, 7);

        RoomType roomType = RoomType.builder().id(10L).capacidad(4).precioNoche(100.0).codigo("DBL").nombre("Doble").build();
        Room room = Room.builder().id(100L).nombre("Hab 301").tipoHabitacion(roomType).build();
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").room(room).build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));
        when(roomRepository.findByTipoHabitacionIdOrderByIdAsc(10L)).thenReturn(List.of(room));
        when(reservationRepository.findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                anyLong(), any(), any(), anyString()))
                .thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Reservation resultado = reservationService.updateReservacion(1L, 10L, inicio, fin, 2);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getFechaInicio()).isEqualTo(inicio);
        assertThat(resultado.getFechaFin()).isEqualTo(fin);
        assertThat(resultado.getCantidadPersonas()).isEqualTo(2);
    }

    @Test
    void ReservationService_updateReservacion_IllegalStateExceptionWhenNotPending() {
        // Arrange
        Reservation reservaActiva = Reservation.builder().id(1L).estado("ACTIVE").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservaActiva));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateReservacion(
                1L, 10L,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 7),
                2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PENDING");
    }

    @Test
    void ReservationService_updateReservacion_IllegalArgumentExceptionWhenCapacidadExcedida() {
        // Arrange
        RoomType roomType = RoomType.builder().id(10L).capacidad(2).precioNoche(100.0).codigo("SGL").nombre("Simple").build();
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateReservacion(
                1L, 10L,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 7),
                10)) // excede capacidad maxima de 2
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidad");
    }

    @Test
    void ReservationService_updateReservacion_IllegalStateExceptionWhenNoRoomsAvailable() {
        // Arrange
        LocalDate inicio = LocalDate.of(2026, 9, 1);
        LocalDate fin = LocalDate.of(2026, 9, 7);

        RoomType roomType = RoomType.builder().id(10L).capacidad(4).precioNoche(100.0).codigo("DBL").nombre("Doble").build();
        Room room = Room.builder().id(100L).nombre("Hab 301").tipoHabitacion(roomType).build();
        Reservation reserva = Reservation.builder().id(1L).estado("PENDING").room(room).build();
        Reservation conflictoOtro = Reservation.builder().id(999L).estado("ACTIVE").build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(roomTypeRepository.findById(10L)).thenReturn(Optional.of(roomType));
        when(roomRepository.findByTipoHabitacionIdOrderByIdAsc(10L)).thenReturn(List.of(room));
        when(reservationRepository.findByRoomIdAndFechaInicioLessThanAndFechaFinGreaterThanAndEstadoNot(
                anyLong(), any(), any(), anyString()))
                .thenReturn(List.of(conflictoOtro));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.updateReservacion(1L, 10L, inicio, fin, 2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("disponibles");
    }

    @Test
    void ReservationService_payAccount_ClosedAccount() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        Account cuenta = Account.builder().id(1L).saldo(200.0).estado("OPEN").reservation(reserva).build();
        AccountItem item1 = AccountItem.builder().id(1L).cantidad(1).eliminado(false).account(cuenta).build();
        AccountItem item2 = AccountItem.builder().id(2L).cantidad(2).eliminado(false).account(cuenta).build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuenta));
        when(accountItemRepository.findByAccountId(1L)).thenReturn(List.of(item1, item2));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account resultado = reservationService.payAccount(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("CLOSED");
        assertThat(item1.getEliminado()).isTrue();
        assertThat(item2.getEliminado()).isTrue();
        verify(accountItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void ReservationService_payAccount_IllegalStateExceptionWhenAlreadyClosed() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("INACTIVE").build();
        Account cuentaCerrada = Account.builder().id(1L).estado("CLOSED").reservation(reserva).build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.of(cuentaCerrada));

        // Act & Assert
        assertThatThrownBy(() -> reservationService.payAccount(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CLOSED");
    }

    @Test
    void ReservationService_payAccount_IllegalStateExceptionWhenNoAccount() {
        // Arrange
        Reservation reserva = Reservation.builder().id(1L).estado("ACTIVE").build();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reserva));
        when(accountRepository.findByReservationId(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> reservationService.payAccount(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cuenta");
    }
}
