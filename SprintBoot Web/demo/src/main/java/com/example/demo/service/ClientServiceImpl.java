package com.example.demo.service;

import com.example.demo.entities.Account;
import com.example.demo.entities.Client;
import com.example.demo.entities.Reservation;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AccountItemRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private static final Set<String> ESTADOS_ACTIVOS = Set.of("PENDING", "CONFIRMED", "ACTIVE");

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private ReservationRepository reservaRepository;

    @Autowired
    private AccountRepository cuentaRepository;

    @Autowired
    private AccountItemRepository itemCuentaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client getClienteById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));
    }

    @Override
    public Client saveCliente(Client cliente) {
        if (cliente.getActivo() == null) {
            cliente.setActivo(true);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Client updateCliente(Long id, Client datos) {
        Client cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));

        if (datos.getNombre() != null) cliente.setNombre(datos.getNombre());
        if (datos.getEmail() != null) cliente.setEmail(datos.getEmail());
        if (datos.getTelefono() != null) cliente.setTelefono(datos.getTelefono());
        if (datos.getFotoPerfil() != null) cliente.setFotoPerfil(datos.getFotoPerfil());
        if (datos.getUsuario() != null) cliente.setUsuario(datos.getUsuario());
        if (datos.getContrasena() != null && !datos.getContrasena().isBlank()) {
            cliente.setContrasena(datos.getContrasena());
        }

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCuenta(Long id) {
        Client cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));

        List<Reservation> reservas = reservaRepository.findByClientId(id);

        boolean tieneReservasActivas = reservas.stream()
            .anyMatch(r -> r.getEstado() != null && ESTADOS_ACTIVOS.contains(r.getEstado()));
        if (tieneReservasActivas) {
            throw new IllegalStateException(
                "No puedes eliminar la cuenta porque tienes reservas activas. "
                    + "Cancela o finaliza tus reservas antes de eliminar la cuenta.");
        }

        for (Reservation reserva : reservas) {
            Optional<Account> cuentaOpt = cuentaRepository.findByReservationId(reserva.getId());
            if (cuentaOpt.isEmpty()) {
                continue;
            }
            Account cuenta = cuentaOpt.get();
            boolean cuentaAbierta = "OPEN".equals(cuenta.getEstado());
            boolean tieneItemsActivos = !itemCuentaRepository
                .findByAccountIdAndEliminadoFalse(cuenta.getId()).isEmpty();
            if (cuentaAbierta && tieneItemsActivos) {
                throw new IllegalStateException(
                    "No puedes eliminar la cuenta porque tienes servicios activos sin pagar. "
                        + "Paga o elimina los servicios de tu cuenta primero.");
            }
        }

        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void deleteCliente(Long id) {
        Client cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No se encontró cliente con ID: " + id, id));

        List<Reservation> reservasAsociadas = reservaRepository.findByClientId(id);
        if (!reservasAsociadas.isEmpty()) {
            String reservasResumen = reservasAsociadas.stream()
                .limit(3)
                .map(reserva -> "#" + reserva.getId() + " (" + reserva.getFechaInicio() + " a " + reserva.getFechaFin() + ")")
                .collect(java.util.stream.Collectors.joining(", "));
            String sufijo = reservasAsociadas.size() > 3
                ? " y " + (reservasAsociadas.size() - 3) + " más"
                : "";

            throw new IllegalStateException(
                "No se puede eliminar el cliente '" + cliente.getNombre() + "' porque tiene "
                    + reservasAsociadas.size() + " reservas asociadas: " + reservasResumen + sufijo
                    + ". Cancela o reasigna las reservas primero."
            );
        }

        clienteRepository.delete(cliente);
    }
}
