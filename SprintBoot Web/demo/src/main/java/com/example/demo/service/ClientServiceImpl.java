package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.entities.Reservation;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private ReservationRepository reservaRepository;

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
        return clienteRepository.save(cliente);
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
